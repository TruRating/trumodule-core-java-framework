
/*
 *  The MIT License
 *
 *  Copyright (c) 2017 TruRating Ltd. https://www.trurating.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.trurating.trumodule;

import com.trurating.service.v230.xml.*;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.device.IReceiptManager;
import com.trurating.trumodule.messages.PosParams;
import com.trurating.trumodule.messages.TruModuleMessageFactory;
import com.trurating.trumodule.network.HttpClient;
import com.trurating.trumodule.network.ISerializer;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.util.TruModuleDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Details TruModule reference implementation
 * - to the TruService application
 * - retrieve a question,
 * - run a rating question,
 * - issue an appropriate receipt message
 * - and deliver the rating to truService
 * <p>
 * TruModule contains an instance of IDevice, the TruModule wrapper. The wrapper will be activated whenever TruModule needs
 * to access the PED. This can be during the asking of the AQ1KR, or to display a rating's acknowledgement or display the PED's
 * standard welcome message.
 * </p>
 */
public abstract class TruModule {

    //================================================================================
    // Properties
    //================================================================================

    private final Logger logger = LoggerFactory.getLogger(TruModule.class);

    private final ITruModuleProperties truModuleProperties; //TruModule's properties file
    private IDevice iDevice; //The TruModule wrapper around partner specific implementations of the PED
    private IReceiptManager iReceiptManager;
    private HttpClient httpClient = null; //HttpClient for marshalling messages through to TruService via JAXB
    private CountDownLatch dwellTimeLatch = null; // the dwell time latch is used to synchronise TruModule on a rating or timeout, whichever comes first

    private volatile boolean questionRunning = false; // this flags the entrance and exits to the method that displays the question, and is used as a check during dwelltime extend
    private volatile boolean questionCancelled = false;

    private volatile AtomicLong activationRecheck;
    private volatile boolean isActivated;
    private volatile boolean isSuspended;
    private volatile boolean isRegSuccessful;
    private volatile String regCode;
    private volatile Integer activeOutletCount;
    private volatile String sessionId;
    private volatile int dwellTimeExtendMs;

    private Trigger trigger;

    private final Object questionRunningLock = new Object();
    private final Object questionCancelledLock = new Object();

    //================================================================================
    // Constructors
    //================================================================================

    /**
     * Instantiates a new TruModule.
     *
     * @param truModuleProperties the TruModule properties
     * @param iReceiptManager     the receipt manager
     * @param device              the device
     */
    @SuppressWarnings("WeakerAccess")
    public TruModule(ITruModuleProperties truModuleProperties, IReceiptManager iReceiptManager, IDevice device) {
        this(truModuleProperties, iReceiptManager, device, null);
    }

    /**
     * Instantiates a new Tru module.
     *
     * @param truModuleProperties the tru module properties
     * @param iReceiptManager     the receipt manager
     * @param device              the device
     * @param marshaller          the marshaller
     */
    public TruModule(ITruModuleProperties truModuleProperties, IReceiptManager iReceiptManager, IDevice device, ISerializer marshaller) {
        this(truModuleProperties, iReceiptManager, device, marshaller, false);
    }

    /**
     * Instantiates a new TruModule.
     *
     * @param truModuleProperties  the TruModule properties
     * @param iReceiptManager      the receipt manager
     * @param device               the device
     * @param marshaller           the marshaller
     * @param deferActivationCheck the defer activation check
     */
    @SuppressWarnings("WeakerAccess")
    public TruModule(ITruModuleProperties truModuleProperties, IReceiptManager iReceiptManager, IDevice device, ISerializer marshaller, boolean deferActivationCheck) {
        this.logger.debug("Loading TruModule...");
        this.truModuleProperties = truModuleProperties;
        if (marshaller != null) {
            this.httpClient = new HttpClient(truModuleProperties, marshaller);
        } else {
            this.httpClient = new HttpClient(truModuleProperties);
        }
        if (device != null) {
            this.setIDevice(device);
        }
        if (iReceiptManager != null) {
            this.setIReceiptManager(iReceiptManager);
        }
        activationRecheck = new AtomicLong(0);
        if (!deferActivationCheck) {
            try {
                this.isActivated(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //================================================================================
    // Accessors
    //================================================================================

    /**
     * Gets logger.
     *
     * @return the logger
     */
    @SuppressWarnings("WeakerAccess")
    Logger getLogger() {
        return this.logger;
    }

    /**
     * Gets TruModule properties.
     *
     * @return the TruModule properties
     */
    ITruModuleProperties getTruModuleProperties() {
        return this.truModuleProperties;
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    IDevice getIDevice() {
        return this.iDevice;
    }

    /**
     * Set iDevice.
     *
     * @param device the device
     */
    @SuppressWarnings("WeakerAccess")
    public void setIDevice(IDevice device) {
        this.iDevice = device;
    }

    /**
     * Gets i receipt manager.
     *
     * @return the i receipt manager
     */
    IReceiptManager getIReceiptManager() {
        return this.iReceiptManager;
    }

    /**
     * Sets i receipt manager.
     *
     * @param receiptManager the receipt manager
     */
    @SuppressWarnings("WeakerAccess")
    public void setIReceiptManager(IReceiptManager receiptManager) {
        this.iReceiptManager = receiptManager;
    }

    private boolean isQuestionRunning() {
        synchronized (this.questionRunningLock) {
            return this.questionRunning;
        }
    }

    private void setQuestionRunning(boolean questionRunning) {
        synchronized (this.questionRunningLock) {
            this.questionRunning = questionRunning;
        }
    }

    /**
     * Is question cancelled boolean.
     *
     * @return the boolean
     */
    boolean isQuestionCancelled() {
        synchronized (this.questionCancelledLock) {
            return this.questionCancelled;
        }
    }

    private void setQuestionCancelled(boolean questionCancelled) {
        synchronized (this.questionCancelledLock) {
            this.questionCancelled = questionCancelled;
        }
    }

    /**
     * Gets session id.
     *
     * @return the session id
     */
    String getSessionId() {
        return this.getSessionId(null);
    }

    /**
     * Gets session id.
     *
     * @param params the params
     * @return the session id
     */
    String getSessionId(PosParams params) {
        if (params == null) {
            if (this.sessionId == null) {
                return Long.toString(TruModuleDateUtils.getInstance().timeNowMillis());
            }
            return this.sessionId;
        }
        return params.getSessionId();
    }

    /**
     * Sets session id.
     *
     * @param sessionId the session id
     */
    void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Gets trigger.
     *
     * @return the trigger
     */
    Trigger getTrigger() {
        return this.trigger;
    }

    /**
     * Sets trigger.
     *
     * @param trigger the trigger
     */
    void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    //================================================================================
    // Public methods
    //================================================================================

    /**
     * Is activated boolean.
     *
     * @return the boolean
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isActivated() {
        try {
            return this.isActivated(false);
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public boolean isActivatedIgnoreTTL() throws Exception{
        this.activationRecheck.set(0);
        return this.isActivated(true);
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public String getSuspendedRegCode() {
        try {
            return this.regCode;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public Integer getActiveOutletCount() {
        return this.activeOutletCount;
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public boolean isSuspended() {
        return this.isSuspended;
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public boolean isRegSuccessful() {
        return this.isRegSuccessful;
    }

    /**
     * Cancel rating.
     *
     * @noinspection WeakerAccess
     */
    public void cancelRating() {
        this.setQuestionCancelled(true); //Set flag to show question has been cancelled
        if (this.isQuestionRunning()) {
            if (this.getTrigger() == Trigger.DWELLTIMEEXTEND) {
                this.getLogger().info("Waiting " + this.dwellTimeExtendMs + " to cancel rating");
                this.dwellTimeLatch = new CountDownLatch(1);
                try {
                    this.dwellTimeLatch.await(this.dwellTimeExtendMs, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    this.getLogger().warn("Error setting dwellTimeLatch.await()", e);
                }
                if (this.isQuestionRunning()) {
                    if (this.iDevice != null) {
                        this.iDevice.resetDisplay(); //Force the 1AQ1KR loop to exit and release control of the PED
                    }
                }
                this.dwellTimeLatch = null;
            } else {
                if (this.iDevice != null) {
                    this.iDevice.resetDisplay(); //Force the 1AQ1KR loop to exit and release control of the PED
                }
            }
        }
    }

    /**
     * Activate.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public void activate() {
        if (this.isActivated()) {
            return;
        }

        this.getIDevice().displayMessage("This device is not registered");

        String registrationCode = this.getIDevice().readLine("Type your registration code, Press ENTER to register via form input or type SKIP to skip registration");

        boolean result;
        if (registrationCode == null || registrationCode.equals("")) {

            int sectorNode = -1;
            String timeZone = null;
            String emailAddress = null;
            String password = null;
            String address = null;
            String mobileNumber = null;
            String merchantName = null;
            String businessName = null;

            Hashtable<Integer, String> sectorOptions = this.getLookupHashtable(LookupName.SECTORNODE);

            while (sectorNode < 0) {
                String possibleInt = this.getIDevice().readLine("Please pick a numbered " + LookupName.SECTORNODE);

                try {


                    int selectedOptionNumber = Integer.parseInt(possibleInt);

                    if (sectorOptions.containsKey(selectedOptionNumber)) {

                        sectorNode = Integer.parseInt(sectorOptions.get(selectedOptionNumber));


                    }
                } catch (Exception e) {
                    this.getLogger().info("Sector node option cannot be parsed");
                }
            }

            Hashtable<Integer, String> timeZoneOptions = this.getLookupHashtable(LookupName.TIMEZONE);

            while (timeZone == null) {
                String possibleInt = this.getIDevice().readLine("Please pick a numbered " + LookupName.TIMEZONE);
                try {

                    int selectedOptionNumber = Integer.parseInt(possibleInt);
                    if (timeZoneOptions.containsKey(selectedOptionNumber)) {
                        timeZone = timeZoneOptions.get(selectedOptionNumber);
                    }
                } catch (Exception e) {
                    this.getLogger().info("Time zone option cannot be parsed");
                }
            }

            while (emailAddress == null || emailAddress.equals("")) {
                emailAddress = this.getIDevice().readLine("Enter your email address (required)");
            }
            while (password == null || password.equals("")) {
                password = this.getIDevice().readLine("Enter a password (required)");
            }
            while (address == null || address.equals("")) {
                address = this.getIDevice().readLine("Enter your postal address");
            }
            while (mobileNumber == null || mobileNumber.equals("")) {
                mobileNumber = this.getIDevice().readLine("Enter your mobile number, e.g. +44 (1234) 787123");
            }
            while (businessName == null || businessName.equals("")) {
                businessName = this.getIDevice().readLine("Enter your business name, e.g. McDonalds");
            }
            while (merchantName == null || merchantName.equals("")) {
                merchantName = this.getIDevice().readLine("Enter your outlet name, e.g. McDonalds Fleet Street");
            }

            result = this.activate(sectorNode, timeZone, PaymentInstant.PAYBEFORE, emailAddress, password, address, mobileNumber, merchantName, businessName);
        } else {
            result = this.activate(registrationCode);
        }
        if (result) {
            this.getIDevice().displayMessage("This device is activated");
        } else {
            this.getIDevice().displayMessage("This device is not activated");
        }
    }

    /**
     * Activate boolean.
     *
     * @param registrationCode the registration code
     * @return the boolean
     */
    @SuppressWarnings("WeakerAccess")
    public boolean activate(String registrationCode) {
        if (this.isActivated()) {
            return true;
        }

        if (registrationCode.equalsIgnoreCase("SKIP")) {
            return this.isActivated;
        }

        Response response = this.sendRequest(TruModuleMessageFactory.assembleRequestActivate(this.getIDevice(), this.getIReceiptManager(), this.getTruModuleProperties().getPartnerId(), this.getTruModuleProperties().getMerchantId(), this.getTruModuleProperties().getTerminalId(), this.getSessionId(), registrationCode));
        return response != null && this.processStatusResponse(response.getStatus());
    }

    /**
     * Activate boolean.
     *
     * @param sectorNode     the sector node
     * @param timeZone       the time zone
     * @param paymentInstant the payment instant
     * @param emailAddress   the email address
     * @param password       the password
     * @param address        the address
     * @param mobileNumber   the mobile number
     * @param merchantName   the merchant name
     * @param businessName   the business name
     * @return the boolean
     */
    @SuppressWarnings("WeakerAccess")
    public boolean activate(int sectorNode,
                            String timeZone,
                            @SuppressWarnings("SameParameterValue") PaymentInstant paymentInstant,
                            String emailAddress,
                            String password,
                            String address,
                            String mobileNumber,
                            String merchantName,
                            String businessName) {
        if (this.isActivated()) {
            return true;
        }
        Response response = this.sendRequest(TruModuleMessageFactory.assembleRequestActivate(this.getIDevice(), this.getIReceiptManager(), this.getTruModuleProperties().getPartnerId(), this.getTruModuleProperties().getMerchantId(), this.getTruModuleProperties().getTerminalId(), this.getSessionId(), sectorNode, timeZone, paymentInstant, emailAddress, password, address, mobileNumber, merchantName, businessName));
        return response != null && this.processStatusResponse(response.getStatus());
    }

    /**
     * Login boolean.
     *
     * @param sectorNode     the sector node
     * @param timeZone       the time zone
     * @param paymentInstant the payment instant
     * @param emailAddress   the email address
     * @param password       the password
     * @param address        the address
     * @param mobileNumber   the mobile number
     * @param merchantName   the merchant name
     * @param businessName   the business name
     * @return the boolean
     */
    @SuppressWarnings("WeakerAccess")
    public boolean login(int sectorNode,
                            String timeZone,
                            @SuppressWarnings("SameParameterValue") PaymentInstant paymentInstant,
                            String emailAddress,
                            String password,
                            String address,
                            String mobileNumber,
                            String merchantName,
                            String businessName) {
        if (this.isActivated()) {
            return true;
        }
        Response response = this.sendRequest(TruModuleMessageFactory.assembleRequestLogin(this.getIDevice(), this.getIReceiptManager(), this.getTruModuleProperties().getPartnerId(), this.getTruModuleProperties().getMerchantId(), this.getTruModuleProperties().getTerminalId(), this.getSessionId(), sectorNode, timeZone, paymentInstant, emailAddress, password, address, mobileNumber, merchantName, businessName));
        this.isRegSuccessful = false;
        if(response != null){
            this.processStatusResponse(response.getStatus());
        }
        return isRegSuccessful;
    }

    //================================================================================
    // Protected methods
    //================================================================================


    @SuppressWarnings("unused")
    public void forceAcivationCheck() throws Exception{
        this.isActivated(true);
    }

    /**
     * Send transaction.
     *
     * @param sessionId          the session id
     * @param requestTransaction the request transaction
     */
    void sendTransaction(String sessionId, RequestTransaction requestTransaction) {

        try {
            Request request = TruModuleMessageFactory.assembleRequestTransaction(
                    this.truModuleProperties.getPartnerId(),
                    this.truModuleProperties.getMerchantId(),
                    this.truModuleProperties.getTerminalId(),
                    sessionId,
                    requestTransaction);

            if (request != null) {
                this.httpClient.send(this.truModuleProperties.getTruServiceURL(), request);
            }
        } catch (Exception e) {
            this.logger.error("Error sending rating", e);
        }
    }

    /**
     * Send request response.
     *
     * @param request the request
     * @return the response
     */
    Response sendRequest(Request request) {
        return this.httpClient.send(this.truModuleProperties.getTruServiceURL(), request);
    }

    /**
     * Do rating string.
     *
     * @param request the request
     */
    void doRating(final Request request) {
        Response response = getQuestion(request);
        if (response == null) {
            this.logger.info("Response was null");
            return;
        }

        this.setQuestionCancelled(false);

        ResponseLanguage responseLanguage = filterResponseLanguage(response, this.truModuleProperties.getRFC());
        if (responseLanguage == null) {
            this.logger.error("The service returned no language for the sought RFC");
            return;
        }

        this.setTrigger(request.getQuestion().getTrigger()); //assume it's the request trigger that is king. Then if the service provides a trigger use this instead.
        if (response.getEvent() != null) {
            this.setTrigger(response.getEvent().getQuestion().getTrigger());
        }

        if (responseLanguage.getQuestion() == null) {
            this.logger.error("Question was null");
            return;
        }

        try {
            //used to ascertain the length of time that a rating takes
            final long startTime = System.currentTimeMillis();

            final int keyStroke = collectRatingFromDevice(responseLanguage.getQuestion().getValue(), responseLanguage.getQuestion().getTimeoutMs());

            final long endTime = System.currentTimeMillis();
            final long totalTimeTaken = endTime - startTime;

            final ITruModuleProperties truModuleProperties = this.truModuleProperties;
            final String sessionId;
            if (request.getSessionId() != null) {
                sessionId = request.getSessionId();
            } else {
                sessionId = this.getSessionId();
            }

            sendRatingData(truModuleProperties.getTruServiceURL(), request.getPartnerId(), request.getMerchantId(), request.getTerminalId(), sessionId, truModuleProperties.getRFC(), keyStroke, totalTimeTaken);

            //if rating wasn't cancelled, then display the appropriate screen response on the ped
            ResponseScreen responseScreen = filterScreenAcknowledgement(responseLanguage, keyStroke);
            ResponseReceipt responseReceipt = filterReceiptAcknowledgement(responseLanguage, keyStroke);
            if (!this.isQuestionCancelled() || (responseScreen != null && responseScreen.isPriority())) {
                if (responseScreen != null) {
                    this.iDevice.displayMessage(responseScreen.getValue());
                    Thread.sleep(responseScreen.getTimeoutMs());
                }
            }

            this.setQuestionRunning(false);

            if (responseReceipt != null) {
                this.getIReceiptManager().appendReceipt(responseReceipt.getValue());
            }
        } catch (Exception e) {
            this.logger.error("Error collecting rating", e);
        }

        if (this.dwellTimeLatch != null) {
            this.dwellTimeLatch.countDown();
        }
    }

    @SuppressWarnings("unused")
    public ResponseLookupLanguage getLookupSectors(){
        return this.getLookupResponse(LookupName.SECTORNODE);
    }

    //================================================================================
    // Private methods
    //================================================================================

    private int collectRatingFromDevice(String questionText, int timeoutMs) {

        this.setQuestionRunning(true);
        int terminalTimeout = timeoutMs;

        if (trigger == Trigger.DWELLTIMEEXTEND) {
            terminalTimeout = Integer.MAX_VALUE;
            this.dwellTimeExtendMs = timeoutMs;
        }

        // Synchronous 1AQ1KR call
        final int keyStroke = this.iDevice.display1AQ1KR(
                questionText,
                terminalTimeout
        );

        this.logger.info("Keystroke came back as : " + Integer.toString(keyStroke));

        return keyStroke;
    }

    private boolean processStatusResponse(ResponseStatus responseStatus) {
        if (responseStatus == null) {
            return false;
        }
        this.activationRecheck.set(TruModuleDateUtils.getInstance().timeNowMillis() + responseStatus.getTimeToLive());
        this.isActivated = responseStatus.isIsActive();
        if(responseStatus.getRegistrationCode() != null){
            this.regCode = responseStatus.getRegistrationCode();
        }
        if(responseStatus.getActiveOutletCount() != null){
            this.activeOutletCount = responseStatus.getActiveOutletCount();
        }

        try{
            this.isSuspended = responseStatus.isIsSuspended();
        }
        catch (Exception e){
            this.isSuspended = false;
        }
        try{
            this.isRegSuccessful = responseStatus.isIsSuccess();
        }
        catch (Exception e){
            this.isRegSuccessful = false;
        }

        return this.isActivated;
    }

    /**
     * Gets question.
     *
     * @param request the request
     * @return the question
     */
    private Response getQuestion(Request request) {
        if (request == null) {
            return null;
        }

        return this.httpClient.send(this.truModuleProperties.getTruServiceURL(), request);
    }

    /**
     * Filter response language response language.
     *
     * @param response                       the response
     * @param currentTransactionLanguageCode the current transaction language code
     * @return the response language
     */
    private ResponseLanguage filterResponseLanguage(Response response, String currentTransactionLanguageCode) {
        try {
            if (response.getDisplay() == null || response.getDisplay().getLanguage() == null) {
                this.logger.warn("Response getDisplay or getLanguage were null");
                return null;
            }
            for (int i = 0; i < response.getDisplay().getLanguage().size(); i++) { //loop through all the possible languages until we get one that matches
                if (response.getDisplay().getLanguage().get(i).getRfc1766().equals(currentTransactionLanguageCode)) {
                    return response.getDisplay().getLanguage().get(i);
                }
            }

            this.logger.warn("No languages matched. There was no question to ask.");
            return null;

        } catch (NullPointerException e) {
            this.logger.error("Error fetching the next question", e);
        }
        return null;
    }

    /**
     * Filter receipt acknowledgement response receipt.
     *
     * @param responseLanguage the response language
     * @param keyStroke        the key stroke
     * @return the response receipt
     */
    private ResponseReceipt filterReceiptAcknowledgement(ResponseLanguage responseLanguage, int keyStroke) {
        if (responseLanguage.getReceipt() != null) {
            for (int i = 0; i < responseLanguage.getReceipt().size(); i++) {
                switch (responseLanguage.getReceipt().get(i).getWhen()) {
                    case RATED:
                        if (keyStroke >= 0) {
                            return responseLanguage.getReceipt().get(i);
                        }
                        break;
                    case NOTRATED:
                        if (keyStroke < 0) {
                            return responseLanguage.getReceipt().get(i);
                        }
                        break;
                }
            }
        }
        return null;
    }

    /**
     * Filter screen acknowledgement response screen.
     *
     * @param responseLanguage the response language
     * @param keyStroke        the key stroke
     * @return the response screen
     */
    private ResponseScreen filterScreenAcknowledgement(ResponseLanguage responseLanguage, int keyStroke) {
        if (responseLanguage.getScreen() != null) {
            for (int i = 0; i < responseLanguage.getScreen().size(); i++) {
                switch (responseLanguage.getScreen().get(i).getWhen()) {
                    case RATED:
                        if (keyStroke >= 0) {
                            return responseLanguage.getScreen().get(i);
                        }
                        break;
                    case NOTRATED:
                        if (keyStroke < 0) {
                            return responseLanguage.getScreen().get(i);
                        }
                        break;
                }
            }
        }
        return null;
    }

    /**
     * Send rating data response.
     *
     * @param url            the url
     * @param partnerId      the partner id
     * @param merchantId     the merchant id
     * @param terminalId     the terminal id
     * @param sessionId      the session id
     * @param rfc1766        the rfc 1766
     * @param keyStroke      the key stroke
     * @param totalTimeTaken the total time taken
     */
    private void sendRatingData(URL url,
                                String partnerId,
                                String merchantId,
                                String terminalId,
                                String sessionId,
                                String rfc1766,
                                int keyStroke,
                                long totalTimeTaken) {
        try {
            Request request = TruModuleMessageFactory.assembleRatingsDeliveryRequest(rfc1766, partnerId, merchantId, terminalId, sessionId, (short) keyStroke, (int) totalTimeTaken);
            if (request != null) {
                this.httpClient.send(url, request);

            } else {
                this.logger.warn("Returning a null response");
            }

        } catch (Exception e) {
            this.logger.error("Error sending rating", e);
        }
    }

    private boolean isActivated(boolean force) throws Exception{
        if (this.activationRecheck.get() > TruModuleDateUtils.getInstance().timeNowMillis()) {
            getLogger().info("Not querying TruModule status, next check at " + this.activationRecheck + ". IsActive is " + (this.isActivated ? "true" : "false"));
            return this.isActivated;
        }
        Response response = this.sendRequest(TruModuleMessageFactory.assembleRequestQuery(this.getIReceiptManager(),this.getIDevice(),this.getTruModuleProperties().getPartnerId(), this.getTruModuleProperties().getMerchantId(), this.getTruModuleProperties().getTerminalId(), this.getSessionId(), force));
        if (response != null) {
            ResponseStatus responseStatus = response.getStatus();
            return this.processStatusResponse(responseStatus);
        }
        else{
            throw new Exception("Error retrieving TruModule status");
        }
    }



    private Hashtable<Integer, String> getLookupHashtable(LookupName lookupName) {
        Hashtable<Integer, String> result = new Hashtable<Integer, String>();
        int optionNumber = 0;

        ResponseLookupLanguage language = getLookupResponse(lookupName);

        if(language != null) {
            if (language.getOption() != null) {
                for (LookupOption option : language.getOption()) {
                    result.putAll(this.printLookUps(option, 1, optionNumber++));
                }
            }
        }

        return result;
    }

    private ResponseLookupLanguage getLookupResponse(LookupName lookupName){
        Response response = this.sendRequest(TruModuleMessageFactory.assembleRequestlookup(this.getIDevice(), this.getIReceiptManager(), this.getTruModuleProperties().getPartnerId(), this.getTruModuleProperties().getMerchantId(), this.getTruModuleProperties().getTerminalId(), this.getSessionId(), lookupName));

        if (response != null && response.getLookup() != null) {

            for (ResponseLookupLanguage language : response.getLookup().getLanguage()) {
                if (language.getRfc1766().equals(this.getIDevice().getCurrentLanguage())) {
                    if (language.getOption() != null) {
                        return language;
                    }
                }
            }
        }

        return null;
    }

    private Map<Integer, String> printLookUps(LookupOption lookupOption, int depth, int optionNumber) {
        Hashtable<Integer, String> result = new Hashtable<Integer, String>();

        if (lookupOption.getValue() != null) {

            result.put(optionNumber, lookupOption.getValue());
        }
        this.getIDevice().displayMessage((lookupOption.getValue() == null ? "N/A" : "\"" + optionNumber + "\"") +
                String.format("%1$-" + depth + "s", " ") + lookupOption.getText() + " (" + lookupOption.getValue() + ")");
        if (lookupOption.getOption() != null) {

            for (LookupOption option : lookupOption.getOption()) {

                result.putAll(this.printLookUps(option, depth + 1, optionNumber++));
            }
        }
        return result;
    }
}
