
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

import com.trurating.service.v210.xml.*;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.network.HttpClient;
import com.trurating.trumodule.network.IMarshaller;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.util.TruRatingMessageFactory;

import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * Details TruModule reference implementation
 * - to the TruService application
 * - retrieve a question,
 * - run a rating question,
 * - check for a prize,
 * - issue an appropriate receipt message
 * - and deliver the rating to truService
 * <p>
 * TruModule contains an instance of IDevice, the TruModule wrapper. The wrapper will be activated whenever TruModule needs
 * to access the PED. This can be during the asking of the AQ1KR, or to display a rating's acknowledgement or display the PED's
 * standard welcome message.
 * </p>
 */
public abstract class TruModule {

    private final Logger logger = Logger.getLogger(TruModule.class.getName()); //Log4J is used, but feel free to use any logging framework.

    private ITruModuleProperties truModuleProperties; //TruModule's properties file
    private IDevice iDevice; //The TruModule wrapper around partner specific implementations of the PED
    private HttpClient httpClient = null; //HttpClient for marshalling messages through to TruService via JAXB
    private CountDownLatch dwellTimeLatch = null; // the dwell time latch is used to synchronise TruModule on a rating or timeout, whichever comes first

    /**
     * The Tru rating message factory.
     */
    TruRatingMessageFactory truRatingMessageFactory = new TruRatingMessageFactory();
    private volatile boolean questionRunning = false; // this flags the entrance and exits to the method that displays the question, and is used as a check during dwelltime extend
    private volatile boolean isCancelled = false;
    private volatile String sessionId;
    private String receiptTextCache =""; //this only exists for the unique scenario seen in use case 8.
    private volatile int dwellTimeExtendMs;

    /**
     * The enum Module status.
     */
    @SuppressWarnings("unused")
    public enum ModuleStatus{
        /**
         * User cancelled module status.
         */
        USER_CANCELLED,
        /**
         * Question timeout module status.
         */
        QUESTION_TIMEOUT,
        /**
         * Module error module status.
         */
        MODULE_ERROR
    }

    /**
     * Instantiates a new Tru module.
     *
     * @param truModuleProperties the tru module properties
     * @param device              the device
     */
    public TruModule(ITruModuleProperties truModuleProperties, IDevice device){
        this(truModuleProperties,device,null);
    }

    /**
     * Instantiates a new Tru module.
     *
     * @param truModuleProperties the tru module properties
     * @param device              the device
     * @param marshaller          the marshaller
     */
    public TruModule(ITruModuleProperties truModuleProperties, IDevice device, IMarshaller marshaller) {
        this.logger.info("Loading TruModule");
        this.truModuleProperties = truModuleProperties;
        if(marshaller != null){
            this.httpClient = new HttpClient(truModuleProperties,marshaller);
        }
        else{
            this.httpClient = new HttpClient(truModuleProperties);
        }
        if(device != null){
            this.iDevice = device;
        }
    }

    /**
     * Sets receipt text cache.
     *
     * @param receiptTextCache the receipt text cache
     */
    @SuppressWarnings("unused")
    void setReceiptTextCache(String receiptTextCache) {
        this.receiptTextCache = receiptTextCache;
    }

    /**
     * Gets receipt text cache.
     *
     * @return the receipt text cache
     */
    public String getReceiptTextCache() {
        return this.receiptTextCache;
    }

    /**
     * Register i device.
     *
     * @param device the device
     */
    @SuppressWarnings("unused")
    public void registerIDevice(IDevice device){
        this.iDevice = device;
    }

    /**
     * Gets logger.
     *
     * @return the logger
     */
    protected Logger getLogger() {
        return this.logger;
    }

    /**
     * Gets tru module properties.
     *
     * @return the tru module properties
     */
    ITruModuleProperties getTruModuleProperties() {
        return this.truModuleProperties;
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    protected IDevice getIDevice() {
        return this.iDevice;
    }

    /**
     * Set iDevice.
     *
     * @param device the device
     */
    protected void setIDevice(IDevice device){
        this.iDevice = device;
    }

    /**
     * Gets http client.
     *
     * @return the http client
     */
   private  HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Is question running boolean.
     *
     * @return the boolean
     */
    @SuppressWarnings("unused")
    public boolean isQuestionRunning() {
        return this.questionRunning;
    }

    /**
     * Is cancelled boolean.
     *
     * @return the boolean
     */
    @SuppressWarnings("unused")
    public boolean isCancelled() {
        return this.isCancelled;
    }

    /**
     * Gets session id.
     *
     * @param params the params
     * @return the session id
     */
    String getSessionId(PosParams params) {
        if (params==null){
            return this.sessionId;
        }
        return params.getSessionId();
    }

    /**
     * Set session id.
     *
     * @param sessionId the session id
     */
    protected void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }

    /**
     * Gets dwell time extend ms.
     *
     * @return the dwell time extend ms
     */
    @SuppressWarnings("unused")
    public int getDwellTimeExtendMs() {
        return dwellTimeExtendMs;
    }

    /**
     * Send transaction response.
     *
     * @param merchantId         the merchant id
     * @param terminalId         the terminal id
     * @param requestTransaction the request transaction
     * @return the response
     */
    public Response sendTransaction(String merchantId, String terminalId, RequestTransaction requestTransaction) {

        try {
            Request request = this.truRatingMessageFactory.assembleRequestTransaction(
                    this.truModuleProperties.getPartnerId(),
                    merchantId,
                    terminalId,
                    sessionId,
                    requestTransaction);

            if (request != null) {
                return this.getHttpClient().send(this.truModuleProperties.getTruServiceURL(), request);
            } else {
                return null;
            }
        } catch (Exception e) {
            this.logger.severe("Error sending rating");
            this.logger.severe(e.toString());
            return null;
        }
    }

    /**
     * Send pos event response.
     *
     * @param params the params
     * @param event  the event
     * @return the response
     */
    protected Response sendPosEvent(PosParams params, RequestPosEvent event){
        Request request = new Request();
        request.setTerminalId(this.truModuleProperties.getTerminalId());
        request.setSessionId(getSessionId(params));
        request.setPartnerId(getTruModuleProperties().getPartnerId());
        request.setMerchantId(this.truModuleProperties.getMerchantId());
        request.setPosEvent(event);
        return this.getHttpClient().send(this.truModuleProperties.getTruServiceURL(),request);
    }

    /**
     * Send pos event list response.
     *
     * @param params    the params
     * @param eventList the event list
     * @return the response
     */
    protected Response sendPosEventList(PosParams params, RequestPosEventList eventList){
        Request request = new Request();
        request.setTerminalId(this.truModuleProperties.getTerminalId());
        request.setSessionId(getSessionId(params));
        request.setPartnerId(getTruModuleProperties().getPartnerId());
        request.setMerchantId(this.truModuleProperties.getMerchantId());
        request.setPosEventList(eventList);
        return this.getHttpClient().send(this.truModuleProperties.getTruServiceURL(),request);
    }

    /**
     * Cancel rating.
     */
    @SuppressWarnings("WeakerAccess")
    public void cancelRating() {
        this.isCancelled = true; //Set flag to show question has been cancelled
        if (this.iDevice != null) {
            this.iDevice.resetDisplay(); //Force the 1AQ1KR loop to exit and release control of the PED
        }
    }

    /**
     * `
     * Cancel rating dwell time extend.
     *
     */
    @SuppressWarnings("unused")
    public void cancelRatingDwellTimeExtend() {
        this.isCancelled = true; //Set flag to show question has been cancelled - ensures that acknowledgement is skipped unless is a priority (i.e. prize)
        try {
            if (this.questionRunning) {
                this.dwellTimeLatch = new CountDownLatch(1);
                this.dwellTimeLatch.await(this.dwellTimeExtendMs, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException ignored) {

        }

        this.cancelRating();
        this.dwellTimeLatch = null;
    }

    /**
     * Gets question.
     *
     * @param request the request
     * @return the question
     */
    Response getQuestion(Request request) {
        if (request == null) {
            return null;
        }

        return this.getHttpClient().send(this.truModuleProperties.getTruServiceURL(), request);
    }

    /**
     * Do rating string.
     *
     * @param request the request
     * @return the string
     */
    String doRating(final Request request) {
        Response response = getQuestion(request);
        if (response==null) {
            this.logger.info("Response was null");
            return null;
        }

        this.isCancelled = false;
        String result = null;

        ResponseLanguage responseLanguage = filterResponseLanguage(response, this.truModuleProperties.getRFC());
        if (responseLanguage == null) {
            this.logger.severe("The service returned no language for the sought RFC");
            return null;
        }

        Trigger trigger=request.getQuestion().getTrigger(); //assume it's the request trigger that is king. Then if the service provides a trigger use this instead.
        if (response.getEvent()!=null) {
            trigger = response.getEvent().getQuestion().getTrigger(); //this can be a question, or a clear, or nothing
        }

        if (responseLanguage.getQuestion() == null) {
            this.logger.severe("Question was null");
            return null;
        }

        try {
            //used to ascertain the length of time that a rating takes
            final long startTime = System.currentTimeMillis();

            this.questionRunning = true;

            int timeoutMs = responseLanguage.getQuestion().getTimeoutMs();

            if(trigger == Trigger.DWELLTIMEEXTEND){
                timeoutMs= Integer.MAX_VALUE;
                this.dwellTimeExtendMs = responseLanguage.getQuestion().getTimeoutMs();
            }

            // Synchronous 1AQ1KR call
            final int keyStroke = this.iDevice.display1AQ1KR(
                    responseLanguage.getQuestion().getValue(),
                    timeoutMs
            );
            this.logger.info("Keystroke came back as : " + Integer.toString(keyStroke));

            final long endTime = System.currentTimeMillis();
            final long totalTimeTaken = endTime - startTime;

            final ITruModuleProperties truModuleProperties = this.truModuleProperties;
            final String sessionId = this.sessionId;
            new Thread(new Runnable() { //Send rating now
                public void run() {
                    sendRatingData(truModuleProperties.getTruServiceURL(), request.getPartnerId(), request.getMerchantId(), request.getTerminalId(), sessionId, truModuleProperties.getRFC(), keyStroke, totalTimeTaken);
                }
            }).start();

            //if rating wasn't cancelled, then display the appropriate screen response on the ped
            ResponseScreen responseScreen = filterScreenAcknowledgement(responseLanguage, keyStroke);
            ResponseReceipt responseReceipt = filterReceiptAcknowledgement(responseLanguage, keyStroke);
            if (!isCancelled || (responseScreen != null && responseScreen.isPriority())) {
                if (responseScreen != null) {
                    this.iDevice.displayMessage(responseScreen.getValue());
                    Thread.sleep(responseScreen.getTimeoutMs());
                }
            }

            this.questionRunning = false;

            if (responseReceipt != null) {
                result = responseReceipt.getValue();
            }
        } catch (Exception e) {
            this.logger.severe(e.toString());
        }

        if (this.dwellTimeLatch != null){
            this.dwellTimeLatch.countDown();
        }

        return result;
    }


    /**
     * Filter response language response language.
     *
     * @param response                       the response
     * @param currentTransactionLanguageCode the current transaction language code
     * @return the response language
     */
    ResponseLanguage filterResponseLanguage(Response response, String currentTransactionLanguageCode) {
        try {
            if (response.getDisplay() == null || response.getDisplay().getLanguage() == null) {
                this.logger.warning("Response getDisplay or getLanguage were null");
                return null;
            }
            for (int i = 0; i < response.getDisplay().getLanguage().size(); i++) { //loop through all the possible languages until we get one that matches
                if (response.getDisplay().getLanguage().get(i).getRfc1766().equals(currentTransactionLanguageCode)) {
                    return response.getDisplay().getLanguage().get(i);
                }
            }

            this.logger.warning("No languages matched. There was no question to ask.");
            return null;

        } catch (NullPointerException e) {
            this.logger.severe("Error fetching the next question");
            this.logger.severe(e.toString());
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
     * @return the response
     */
    Response sendRatingData(URL url,
                                      String partnerId,
                                      String merchantId,
                                      String terminalId,
                                      String sessionId,
                                      String rfc1766,
                                      int keyStroke,
                                      long totalTimeTaken) {
        try {
            Request request = this.truRatingMessageFactory.assembleRatingsDeliveryRequest(rfc1766, partnerId, merchantId, terminalId, sessionId, (short) keyStroke, (int) totalTimeTaken);
            if (request != null) {
                return this.getHttpClient().send(url, request);
            } else {
                this.logger.warning("Returning a null response");
                return null;
            }

        } catch (Exception e) {
            this.logger.severe("Error sending rating");
            this.logger.severe(e.toString());
            return null;
        }
    }
}
