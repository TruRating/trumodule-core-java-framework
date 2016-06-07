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

    public TruModule(ITruModuleProperties ITruModuleProperties) {
        this.ITruModuleProperties = ITruModuleProperties;
        truRatingMessageFactory = new TruRatingMessageFactory();
        xmlNetworkMessenger = new XMLNetworkMessenger(ITruModuleProperties);
    }

    public void doRating() {
        clearCachedRatingInformation();
        cachedTruModuleRatingObject = new CachedTruModuleRatingObject();
        Request request = truRatingMessageFactory.assembleQuestionRequest(ITruModuleProperties, cachedTruModuleRatingObject.sessionID);
        cachedTruModuleRatingObject = requestQuestionFromServerAndCacheResult(request);
        runQuestion();
    }

    public void doRatingInBackground() {
        clearCachedRatingInformation();
        cachedTruModuleRatingObject = new CachedTruModuleRatingObject();
        Request request = truRatingMessageFactory.assembleQuestionRequest(ITruModuleProperties, cachedTruModuleRatingObject.sessionID);
        cachedTruModuleRatingObject = requestQuestionFromServerAndCacheResult(request);
        new Thread(new Runnable() {
            public void run() {
                runQuestion();
            }
        }).start();
    }

    public boolean deliverRating(RequestTransaction transaction) {
        final Response response;
        try {
            Request request = truRatingMessageFactory.assembleRatingsDeliveryRequest(ITruModuleProperties, cachedTruModuleRatingObject.sessionID);
            request.setTransaction(transaction);
            response = xmlNetworkMessenger.getResponseRatingFromRatingsDeliveryToService(request);
            if (response==null) return false;
            truRatingMessageFactory.assembleRatingsDeliveryRequest(ITruModuleProperties, cachedTruModuleRatingObject.sessionID);
        } catch (Exception e) {
            log.error("", e);
            return false;
        } finally {
            clearCachedRatingInformation();
        }
        log.info("Everything is finished in truModule!");
        return true;
    }

    private CachedTruModuleRatingObject requestQuestionFromServerAndCacheResult(Request request) {
        cachedTruModuleRatingObject.response = xmlNetworkMessenger.getResponseQuestionFromService(request);
        Response response = cachedTruModuleRatingObject.response;
        try {
            String desiredLanguage = ITruModuleProperties.getLanguageCode();
            if (response.getDisplay()==null || response.getDisplay().getLanguage()==null) {
                log.warn("Response getDisplay or getLanguage were null");
                return cachedTruModuleRatingObject;
            }

            for (int i=0; i<response.getDisplay().getLanguage().size(); i++) { //loop through all the possible languages until we get one that matches
                if (response.getDisplay().getLanguage().get(i).getRfc1766().equals(desiredLanguage)) {
                    cachedTruModuleRatingObject.question = response.getDisplay().getLanguage().get(i).getQuestion().getValue();
                    cachedTruModuleRatingObject.receiptNoRating = response.getDisplay().getLanguage().get(i).getReceipt().get(0).getValue();
                    cachedTruModuleRatingObject.receiptWithRating = response.getDisplay().getLanguage().get(i).getReceipt().get(1).getValue();
                    cachedTruModuleRatingObject.responseNoRating = response.getDisplay().getLanguage().get(i).getScreen().get(0).getValue();
                    cachedTruModuleRatingObject.responseWithRating = response.getDisplay().getLanguage().get(i).getScreen().get(1).getValue();
                    return cachedTruModuleRatingObject;
                }
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
                log.warn("Cached truModuleRatingObject was null");
                return;
            }

            final int displayWidth = ITruModuleProperties.getDeviceCPL();
            String qText = cachedTruModuleRatingObject.question;
            if (qText==null) return;

            String[] qTextWraps = qText.split("\\\\n");
            if ((qTextWraps.length == 1) && (qTextWraps[0].length() > displayWidth))
                qTextWraps = StringUtilities.wordWrap(qText, displayWidth);

            int timeout = ITruModuleProperties.getQuestionTimeout();
            if (timeout < 1000)
                timeout = 60000;

            final long startTime = System.currentTimeMillis();
            keyStroke = iDevice.displayTruratingQuestionGetKeystroke(qTextWraps, qText, timeout);
            final long endTime = System.currentTimeMillis();
            totalTimeTaken = endTime - startTime;

            RequestRating rating;
            int ratingValue = new Integer(keyStroke);
            if (ratingValue >= -1 && ratingValue <= 9) {
                rating = new RequestRating();
                rating.setValue(new Short(keyStroke));
                rating.setResponseTimeMs(new Long(totalTimeTaken).intValue());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                rating.setDateTime(sdf.format(new Date()));

                if (ratingValue > -1) {
                    // Update the receipt text to indicate that the user rated
                    String questionText = cachedTruModuleRatingObject.responseWithRating;
                    iDevice.displayMessage(questionText);

                } else {
                    iDevice.displayMessage(cachedTruModuleRatingObject.responseNoRating);
                }
                cachedTruModuleRatingObject.rating=rating;
            }
        } catch (Exception e) {
            log.error("truModule error", e);
        }
    }

    public String getReceiptMessage() {
        if (getCurrentRatingRecord().getValue()>-99) {
            return cachedTruModuleRatingObject.receiptWithRating;
        } else {
            return cachedTruModuleRatingObject.receiptNoRating;
        }
    }

    public void close() {
        clearCachedRatingInformation();
    }

    public void clearCachedRatingInformation() {
        cachedTruModuleRatingObject = null;
    }

    public void setDevice(IDevice deviceRef) {
        this.iDevice = deviceRef;
    }

    public void cancelRating() {
        getDevice().cancelInput();
    }

    public RequestRating getCurrentRatingRecord() {
        return cachedTruModuleRatingObject.rating;
    }

    private IDevice getDevice() {
        return iDevice;
    }

    public static final short NO_RATING_VALUE = -99;
    public static final short USER_CANCELLED = -1;
    public static final short QUESTION_TIMEOUT = -2;
    public static final short NO_QUESTION_ASKED = -3;
}
