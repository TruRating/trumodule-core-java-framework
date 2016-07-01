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
    private final ITruModuleProperties ITruModuleProperties;
    private IDevice iDevice = null;
    private IXMLNetworkMessenger xmlNetworkMessenger = null;
    private TruRatingMessageFactory truRatingMessageFactory = null;
    private static volatile CachedTruModuleRatingObject cachedTruModuleRatingObject;
    private volatile int ratingDeliveryOutcome;

    public static final short USER_CANCELLED = -1;
    public static final short QUESTION_TIMEOUT = -2;
    public static final short NO_QUESTION_ASKED = -3;
    public static final short MODULE_ERROR = -4;
    public static final short NO_VALUE = -99;

    public static final int RATING_DELIVERY_OUTCOME_FAILED = -1;
    public static final int RATING_DELIVERY_OUTCOME_SUCCEEDED = 1;

    public TruModule(ITruModuleProperties ITruModuleProperties) {

        log.info("**** TRUMODULE SETUP ****");

        this.ITruModuleProperties = ITruModuleProperties;
        truRatingMessageFactory = new TruRatingMessageFactory();
        xmlNetworkMessenger = new XMLNetworkMessenger(ITruModuleProperties);
    }

    public void doRating() {
        clearAllCachedModuleData();
        cachedTruModuleRatingObject = new CachedTruModuleRatingObject();
        Request request = truRatingMessageFactory.assembleQuestionRequest(ITruModuleProperties, cachedTruModuleRatingObject.sessionID);
        cachedTruModuleRatingObject = requestQuestionFromServerAndCacheResult(request);
        if (cachedTruModuleRatingObject != null) runQuestion();
    }

    public void doRatingInBackground() {
        clearAllCachedModuleData();
        cachedTruModuleRatingObject = new CachedTruModuleRatingObject();
        Request request = truRatingMessageFactory.assembleQuestionRequest(ITruModuleProperties, cachedTruModuleRatingObject.sessionID);
        cachedTruModuleRatingObject = requestQuestionFromServerAndCacheResult(request);
        if (cachedTruModuleRatingObject != null) new Thread(new Runnable() {
            public void run() {
                runQuestion();
            }
        }).start();
    }

    public int deliverRating() { //todo put this into a background thread...
        log.info("About to deliver a rating");
        ratingDeliveryOutcome = RATING_DELIVERY_OUTCOME_SUCCEEDED;

        if (cachedTruModuleRatingObject==null) {
            log.error("It is not possible to deliver a rating that doesn't exist");
            return  RATING_DELIVERY_OUTCOME_FAILED;
        }

        final Response response;
        try {

            Request request = truRatingMessageFactory.assembleRatingsDeliveryRequest(ITruModuleProperties, cachedTruModuleRatingObject.sessionID);
            if (cachedTruModuleRatingObject.rating.getValue()==TruModule.NO_QUESTION_ASKED) {
                log.info("Sending only transaction as there was NO question to rate against");
                request.setTransaction(cachedTruModuleRatingObject.transaction);
            } else {
                log.info("Sending rating and transaction as there WAS a question to rate against");
                request.setRating(cachedTruModuleRatingObject.rating);
                request.getRating().setTransaction(cachedTruModuleRatingObject.transaction);
            }

            request.getRating().setRfc1766(cachedTruModuleRatingObject.response.getDisplay().getLanguage().get(0).getRfc1766()); //todo this should be multi language capable
            response = xmlNetworkMessenger.getResponseRatingFromRatingsDeliveryToService(request);
            if (response == null) ratingDeliveryOutcome = RATING_DELIVERY_OUTCOME_FAILED;
        } catch (Exception e) {
            log.error("", e);
            ratingDeliveryOutcome = RATING_DELIVERY_OUTCOME_FAILED;
        }
        log.info("Everything is finished in truModule!");
        return ratingDeliveryOutcome;
    }

    private CachedTruModuleRatingObject requestQuestionFromServerAndCacheResult(Request request) {
        cachedTruModuleRatingObject.response = xmlNetworkMessenger.getResponseQuestionFromService(request);
        Response response = cachedTruModuleRatingObject.response; //rfc is available from here
        if (response == null) {
            log.error("Unable to contact the the truRating service for the next question");
            return null;
        }

        try {
            String desiredLanguage = ITruModuleProperties.getLanguageCode();
            if (response.getDisplay() == null || response.getDisplay().getLanguage() == null) {
                log.warn("Response getDisplay or getLanguage were null");
                return cachedTruModuleRatingObject;
            }

            boolean matched = false;
            for (int i = 0; i < response.getDisplay().getLanguage().size(); i++) { //loop through all the possible languages until we get one that matches
                if (response.getDisplay().getLanguage().get(i).getRfc1766().equals(desiredLanguage)) {
                    matched=true;
                    cachedTruModuleRatingObject.question = response.getDisplay().getLanguage().get(i).getQuestion().getValue();
                    cachedTruModuleRatingObject.receiptWithRating = response.getDisplay().getLanguage().get(i).getReceipt().get(0).getValue();
                    cachedTruModuleRatingObject.receiptNoRating = response.getDisplay().getLanguage().get(i).getReceipt().get(1).getValue();
                    cachedTruModuleRatingObject.responseWithRating = response.getDisplay().getLanguage().get(i).getScreen().get(0).getValue();
                    cachedTruModuleRatingObject.responseNoRating = response.getDisplay().getLanguage().get(i).getScreen().get(1).getValue();
                    return cachedTruModuleRatingObject;
                }
            }

            if (!matched) {
                log.warn("No languages matched. There was no question to ask.");
                cachedTruModuleRatingObject=null;
            }

        } catch (NullPointerException e) {
            log.error("Error fetching the next question", e);
        }

        return cachedTruModuleRatingObject;
    }

    private void runQuestion() {
        String keyStroke = String.valueOf(NO_QUESTION_ASKED);
        long totalTimeTaken = 0;

        try {
            if (cachedTruModuleRatingObject.response == null) {
                log.info("Cached truModuleRatingObject was null");
                return;
            }

            final int displayWidth = ITruModuleProperties.getDeviceCPL();
            String qText = cachedTruModuleRatingObject.question;
            if (qText == null) {
                cachedTruModuleRatingObject.rating.setValue(TruModule.NO_QUESTION_ASKED);
                return;
            }

            String[] qTextWraps = qText.split("\\\\n");
            if ((qTextWraps.length == 1) && (qTextWraps[0].length() > displayWidth))
                qTextWraps = StringUtilities.wordWrap(qText, displayWidth);

            int timeout = ITruModuleProperties.getQuestionTimeout();
            if (timeout < 1000)
                timeout = 60000;

            final long startTime = System.currentTimeMillis();
            keyStroke = iDevice.displayTruratingQuestionGetKeystroke(qTextWraps, qText, timeout);

            log.info("KEYSTROKE CAME BACK AS : " + keyStroke);
            final long endTime = System.currentTimeMillis();
            totalTimeTaken = endTime - startTime;

            cachedTruModuleRatingObject.rating.setValue(new Short(keyStroke));
            cachedTruModuleRatingObject.rating.setResponseTimeMs(new Long(totalTimeTaken).intValue());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cachedTruModuleRatingObject.rating.setDateTime(sdf.format(new Date()));

            if (!cachedTruModuleRatingObject.cancelled) {
                if ( cachedTruModuleRatingObject.rating.getValue() > -1)
                    iDevice.displayMessage(cachedTruModuleRatingObject.responseWithRating);

                else
                    iDevice.displayMessage(cachedTruModuleRatingObject.responseNoRating);
            }

        } catch (Exception e) {
            log.error("truModule error", e);
        }
    }

    public String getReceiptMessage() {
        if (cachedTruModuleRatingObject == null || cachedTruModuleRatingObject.receiptWithRating == null || cachedTruModuleRatingObject.receiptNoRating == null)
            return "";
        if (getCurrentRatingRecord().getValue() > -1) { //I've adjusted this as -99 is illegal according to current spec
            return cachedTruModuleRatingObject.receiptWithRating;
        } else {
            return cachedTruModuleRatingObject.receiptNoRating;
        }
    }

    public void clearAllCachedModuleData() {
        cachedTruModuleRatingObject = null;
    }

    @Override
    public void createNewCachedTransaction() {
        if (cachedTruModuleRatingObject == null) {
            log.error("Unable to create a new cached transaction object, as the rating object doesn't exist");
        } else
            cachedTruModuleRatingObject.transaction = new RequestTransaction();
    }

    @Override
    public void updateCachedTransaction(RequestTransaction requestTransaction) {
        if (cachedTruModuleRatingObject == null) {
            log.error("Unable to update the cached transaction object, as one doesn't exist");
        } else
            cachedTruModuleRatingObject.transaction = requestTransaction;
    }

    @Override
    public RequestTransaction getCurrentCachedTransaction() {
        if (cachedTruModuleRatingObject == null) {
            log.warn("An attempt was made to gain the cached current transaction, but the cachedTruModuleRatingObject was null");
            return null;
        }
        return cachedTruModuleRatingObject.transaction;
    }

    public void setDevice(IDevice deviceRef) {
        this.iDevice = deviceRef;
    }

    public void cancelRating() {
        if (cachedTruModuleRatingObject != null) cachedTruModuleRatingObject.cancelled = true;
        if (iDevice != null) getDevice().cancelInput();
    }

    public RequestRating getCurrentRatingRecord() {
        if (cachedTruModuleRatingObject == null) {
            RequestRating requestRating = new RequestRating();
            requestRating.setValue(TruModule.MODULE_ERROR);
            log.warn("An attempt was made to gain the cached current rating record, but the cachedTruModuleRatingObject was null");

            return requestRating;
        } else return cachedTruModuleRatingObject.rating;
    }

    private IDevice getDevice() {
        return iDevice;
    }

    public CachedTruModuleRatingObject getCachedTruModuleRatingObject() {
        return cachedTruModuleRatingObject;
    }
}
