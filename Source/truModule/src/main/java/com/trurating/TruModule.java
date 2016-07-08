/*
 * @(#)TruModule.java
 *
 * Copyright (c) 2016 trurating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * trurating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with trurating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT 
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.trurating;

import com.trurating.network.xml.IXMLNetworkMessenger;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.service.v200.xml.*;
import org.apache.log4j.Logger;

import com.trurating.device.IDevice;
import com.trurating.network.xml.TruRatingMessageFactory;
import com.trurating.network.xml.XMLNetworkMessenger;
import com.trurating.util.StringUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * TruModule is the main class of a library that encapsulates the behaviour
 * required by a payment application to
 * - connect to the truService application
 * - retrieve a question,
 * - run a rating question,
 * - check for a prize,
 * - issue an appropriate receipt message
 * - and deliver the rating to truService
 * Created by Paul on 01/03/2016.
 */
public class TruModule implements ITruModule {

    private final Logger log = Logger.getLogger(TruModule.class);
    private final ITruModuleProperties truModuleProperties;
    private IDevice iDevice = null;
    private IXMLNetworkMessenger xmlNetworkMessenger = null;
    private TruRatingMessageFactory truRatingMessageFactory = null;
    private static volatile CachedTruModuleObject cachedTruModuleObject;
    private volatile int ratingDeliveryOutcome;
    private volatile boolean dwelltimeextendEnacted = false; //this is used by the trpipeline component to flag to state of dwelltimeextend

    public static final short USER_CANCELLED = -1;
    public static final short QUESTION_TIMEOUT = -2;
    public static final short NO_QUESTION_ASKED = -3;
    public static final short MODULE_ERROR = -4;
    public static final short NO_VALUE = -99;
    public static final int RATING_DELIVERY_OUTCOME_FAILED = -1;
    public static final int RATING_DELIVERY_OUTCOME_SUCCEEDED = 1;
    private String currentTransactionlLanguageCode;

    public TruModule(ITruModuleProperties truModuleProperties) {

        log.info("**** TRUMODULE SETUP ****");

        this.truModuleProperties = truModuleProperties;
        this.currentTransactionlLanguageCode = truModuleProperties.getLanguageCode() ;

        truRatingMessageFactory = new TruRatingMessageFactory();
        xmlNetworkMessenger = new XMLNetworkMessenger(truModuleProperties);
    }

    public void doRating() {
        clearAllCachedModuleData();
        cachedTruModuleObject = new CachedTruModuleObject();
        Request request = truRatingMessageFactory.assembleQuestionRequest(truModuleProperties, cachedTruModuleObject.sessionID);
        cachedTruModuleObject = requestQuestionFromServerAndCacheResult(request);
        if (cachedTruModuleObject != null) runQuestion();
    }

    public void doRatingInBackground() {
        clearAllCachedModuleData();
        cachedTruModuleObject = new CachedTruModuleObject();
        Request request = truRatingMessageFactory.assembleQuestionRequest(truModuleProperties, cachedTruModuleObject.sessionID);
        cachedTruModuleObject = requestQuestionFromServerAndCacheResult(request);
        if (cachedTruModuleObject != null) new Thread(new Runnable() {
            public void run() {
                runQuestion();
            }
        }).start();
    }

    public int deliverRating() {
        log.info("About to deliver a rating");
        ratingDeliveryOutcome = RATING_DELIVERY_OUTCOME_SUCCEEDED;

        if (cachedTruModuleObject ==null) {
            log.error("It is not possible to deliver a rating that doesn't exist");
            return  RATING_DELIVERY_OUTCOME_FAILED;
        }

        final Response response;
        try {

            Request request = truRatingMessageFactory.assembleRatingsDeliveryRequest(truModuleProperties, cachedTruModuleObject.sessionID);
            if (cachedTruModuleObject.rating.getValue()==TruModule.NO_QUESTION_ASKED) {
                log.info("Sending only transaction as there was NO question to rate against");
                request.setTransaction(cachedTruModuleObject.transaction);
            } else {
                log.info("Sending rating and transaction as there WAS a question to rate against");
                request.setRating(cachedTruModuleObject.rating);
                request.getRating().setTransaction(cachedTruModuleObject.transaction);
            }

            request.getRating().setRfc1766(currentTransactionlLanguageCode);
            response = xmlNetworkMessenger.getResponseRatingFromRatingsDeliveryToService(request);
            if (response == null) ratingDeliveryOutcome = RATING_DELIVERY_OUTCOME_FAILED;
        } catch (Exception e) {
            log.error("", e);
            ratingDeliveryOutcome = RATING_DELIVERY_OUTCOME_FAILED;
        }
        log.info("Everything is finished in truModule!");
        return ratingDeliveryOutcome;
    }

    public CachedTruModuleObject requestQuestionFromServerAndCacheResult(Request request) {
        cachedTruModuleObject.response = xmlNetworkMessenger.getResponseQuestionFromService(request);
        Response response = cachedTruModuleObject.response; //rfc is available from here
        if (response == null) {
            log.error("Unable to contact the the truRating service for the next question");
            return null;
        }

        try {
            if (response.getDisplay() == null || response.getDisplay().getLanguage() == null) {
                log.warn("Response getDisplay or getLanguage were null");
                return cachedTruModuleObject;
            }

            boolean matched = false;
            for (int i = 0; i < response.getDisplay().getLanguage().size(); i++) { //loop through all the possible languages until we get one that matches
                if (response.getDisplay().getLanguage().get(i).getRfc1766().equals(currentTransactionlLanguageCode)) {

                    matched = true;
                    cachedTruModuleObject.question = response.getDisplay().getLanguage().get(i).getQuestion().getValue();

                    List<ResponseScreen> responseScreenList = response.getDisplay().getLanguage().get(i).getScreen();
                    for (int j=0; j< responseScreenList.size(); j++) {
                        if (responseScreenList.get(j).getWhen().value().equals("RATED")) {
                            cachedTruModuleObject.responseWithRating=responseScreenList.get(j).getValue();
                        }
                        if (responseScreenList.get(j).getWhen().value().equals("NOTRATED")) {
                            cachedTruModuleObject.responseNoRating=responseScreenList.get(j).getValue();
                        }

                        if (responseScreenList.get(j).isPriority()) cachedTruModuleObject.isPriority=true;
                    }

                    List<ResponseReceipt> responseReceiptList = response.getDisplay().getLanguage().get(i).getReceipt();
                    for (int j=0; j< responseScreenList.size(); j++) {
                        if (responseReceiptList.get(j).getWhen().value().equals("RATED")) {
                            cachedTruModuleObject.receiptWithRating=responseReceiptList.get(j).getValue();
                        }
                        if (responseReceiptList.get(j).getWhen().value().equals("NOTRATED")) {
                            cachedTruModuleObject.receiptNoRating=responseReceiptList.get(j).getValue();
                        }
                    }

                    return cachedTruModuleObject;
                }
            }

            if (!matched) {
                log.warn("No languages matched. There was no question to ask.");
                cachedTruModuleObject =null;
            }

        } catch (NullPointerException e) {
            log.error("Error fetching the next question", e);
        }

        return cachedTruModuleObject;
    }

    private void runQuestion() {
        String keyStroke = String.valueOf(NO_QUESTION_ASKED);
        long totalTimeTaken = 0;

        try {
            if (cachedTruModuleObject.response == null) {
                log.info("Cached truModuleRatingObject was null");
                return;
            }

            final int displayWidth = truModuleProperties.getDeviceCPL();
            String qText = cachedTruModuleObject.question;
            if (qText == null) {
                cachedTruModuleObject.rating.setValue(TruModule.NO_QUESTION_ASKED);
                return;
            }

            //split the question over multiple lines by displaywidth
            String[] qTextWraps = qText.split("\\\\n");
            if ((qTextWraps.length == 1) && (qTextWraps[0].length() > displayWidth))
                qTextWraps = StringUtilities.wordWrap(qText, displayWidth);

            //the minimum length of time to display the question is 1 sec, any less than this and we default to a minute
            int timeout = truModuleProperties.getQuestionTimeout();
            if (timeout < 1000)
                timeout = 60000;

            final long startTime = System.currentTimeMillis(); //used to ascertain the length of time that a rating takes
            keyStroke = iDevice.displayTruratingQuestionGetKeystroke(qTextWraps, qText, timeout); //call to the aci ped device driver

            log.debug("Keystroke came back as : " + keyStroke);
            final long endTime = System.currentTimeMillis();
            totalTimeTaken = endTime - startTime;

            //cache the rating value and data
            cachedTruModuleObject.rating.setValue(new Short(keyStroke));
            cachedTruModuleObject.rating.setResponseTimeMs(new Long(totalTimeTaken).intValue());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cachedTruModuleObject.rating.setDateTime(sdf.format(new Date()));

            //if rating wasn't cancelled, then display the appropriate screen response on the ped
            if (!cachedTruModuleObject.cancelled) {
                if (dwelltimeextendEnacted==true) {
                    if (!cachedTruModuleObject.isPriority) {
                        log.info("No screen response required as dwelltimeextend was enacted, and this is not a priority message!");
                        return;
                    }
                }
                if (cachedTruModuleObject.rating.getValue() > -1)
                    iDevice.displayMessage(cachedTruModuleObject.responseWithRating);
                else
                    iDevice.displayMessage(cachedTruModuleObject.responseNoRating);
            }

        } catch (Exception e) {
            log.error("truModule error", e);
        }
    }

    public void clearAllCachedModuleData() {
        cachedTruModuleObject = null;
    }

    @Override
    public void createNewCachedTransaction() {
        if (cachedTruModuleObject == null) {
            log.error("Unable to create a new cached transaction object, as the rating object doesn't exist");
        } else
            cachedTruModuleObject.transaction = new RequestTransaction();
    }

    @Override
    public void updateCachedTransaction(RequestTransaction requestTransaction) {
        if (cachedTruModuleObject == null) {
            log.error("Unable to update the cached transaction object, as one doesn't exist");
        } else
            cachedTruModuleObject.transaction = requestTransaction;
    }

    @Override
    public RequestTransaction getCurrentCachedTransaction() {
        if (cachedTruModuleObject == null) {
            log.warn("An attempt was made to gain the cached current transaction, but the cachedTruModuleObject was null");
            return null;
        }
        return cachedTruModuleObject.transaction;
    }

    public void setDevice(IDevice deviceRef) {
        this.iDevice = deviceRef;
    }

    public void cancelRating() {
        if (cachedTruModuleObject != null) cachedTruModuleObject.cancelled = true;
        if (iDevice != null) getDevice().cancelInput();
    }

    public RequestRating getCurrentRatingRecord() {
        if (cachedTruModuleObject == null) {
            RequestRating requestRating = new RequestRating();
            requestRating.setValue(TruModule.MODULE_ERROR);
            log.warn("An attempt was made to gain the cached current rating record, but the cachedTruModuleObject was null");

            return requestRating;
        } else return cachedTruModuleObject.rating;
    }

    private IDevice getDevice() {
        return iDevice;
    }

    public CachedTruModuleObject getCachedTruModuleRatingObject() {
        return cachedTruModuleObject;
    }

    public String getCurrentTransactionLanguageCode() {
        return currentTransactionlLanguageCode;
    }

    public void setCurrentTransactionLanguageCode(String currentTransactionlLanguageCode) {
        this.currentTransactionlLanguageCode = currentTransactionlLanguageCode;
    }

    public String getReceiptMessage() {
        if (cachedTruModuleObject == null || cachedTruModuleObject.receiptWithRating == null || cachedTruModuleObject.receiptNoRating == null)
            return "";
        if (getCurrentRatingRecord().getValue() > -1) { //I've adjusted this as -99 is illegal according to current spec
            return cachedTruModuleObject.receiptWithRating;
        } else {
            return cachedTruModuleObject.receiptNoRating;
        }
    }

    public boolean isDwelltimeextendEnacted() {
        return dwelltimeextendEnacted;
    }

    public void setDwelltimeextendEnacted(boolean dwelltimeextendEnacted) {
        this.dwelltimeextendEnacted = dwelltimeextendEnacted;
    }
}
