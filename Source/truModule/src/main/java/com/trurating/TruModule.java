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
import com.trurating.service.v200.xml.*;
import com.trurating.xml.LanguageManager;
import org.apache.log4j.Logger;

import com.trurating.device.IDevice;
import com.trurating.network.xml.TruRatingMessageFactory;
import com.trurating.network.xml.XMLNetworkMessenger;
import com.trurating.prize.PrizeManagerService;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.util.StringUtilities;

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
 * <p/>
 * Created by Paul on 01/03/2016.
 */
public class TruModule implements ITruModule {

    public final static short NO_RATING_VALUE = -99;
    public final static short USER_CANCELLED = -1;
    public final static short QUESTION_TIMEOUT = -2;
    public final static short NO_QUESTION_ASKED = -3;
    private final Logger log = Logger.getLogger(TruModule.class);
    private final PrizeManagerService prizeManagerService = new PrizeManagerService();
    private IDevice iDevice = null;
    private IXMLNetworkMessenger xmlNetworkMessenger = null;
    private TruRatingMessageFactory truRatingMessageFactory = null;
    private String receiptMessage = "";
    private volatile Request currentRequest = null;
    private Response response = null;
    private String sessionID = "";


    public TruModule() {
        truRatingMessageFactory = new TruRatingMessageFactory();
        xmlNetworkMessenger = new XMLNetworkMessenger();
    }

    public RequestRating getCurrentRatingRecord(ITruModuleProperties properties) {
        if (currentRequest == null) {
            sessionID = Long.toString(new Date().getTime());
            currentRequest = truRatingMessageFactory.assembleRatingsDeliveryRequest(properties, sessionID);
        }
        return currentRequest.getRating();
    }

    /**
     * Request to run a rating question
     * If this needs to be done in a background thread then the assumption is
     * that the thread will already have been created and this call will have been made from it.
     */
    public void doRating(ITruModuleProperties properties) {
        // Ensure that we are starting a new rating record - nothing left over from before
        clearValueOfCachedRatingAndReceipt();
        // The rating class generates its own unique (for this till) transaction id
        final Response response = getQuestionFromService(properties);
        //if there is a question, run it
        runQuestion(properties, response);
    }

    /**
     * Dwell time ratings questions need to be run in a separate thread
     */
    public void doRatingInBackground(final ITruModuleProperties properties) {
        // Ensure that we are starting a new rating record - nothing left over from before
        clearValueOfCachedRatingAndReceipt();
        // The rating class generates its own unique (for this till) transaction id
        final Response response = getQuestionFromService(properties);
        //if there is a question, run it
        new Thread(new Runnable() {
            public void run() {
                runQuestion(properties, response);
            }
        }).start();
    }

    /**
     * Clear the question ready for payment
     */
    public void cancelRating() {
        getDevice().cancelInput();
    }

    public boolean deliverRating(ITruModuleProperties properties) {
        try {
            //send the rating
            final Response response = xmlNetworkMessenger.getResponseFromService(currentRequest, properties);
            final ResponseLanguage language = new LanguageManager().getLanguage(response, properties.getLanguageCode());
            ResponseReceipt ratingResponseReceipt = null;
            if (language != null) {
                ratingResponseReceipt = language.getReceipt().get(0);
            }
            //if there was a receipt for that language, and there is a rating record available
            if (ratingResponseReceipt != null && currentRequest != null) {
                if (currentRequest.getRating().getValue() > 0) {
                    receiptMessage = ratingResponseReceipt.getValue();
                } else {
                    receiptMessage = ratingResponseReceipt.getValue(); //...
                }
            }
        } catch (Exception e) {
            log.error("", e);
            return false;
        } finally {
            clearValueOfCachedRatingAndReceipt();
        }
        log.info("Everything is finished in truModule!");
        return true;
    }

    private Response getQuestionFromService(ITruModuleProperties properties) {

        try {
            response = xmlNetworkMessenger.getResponseFromService(currentRequest, properties);
            ResponseLanguage language =
                    new LanguageManager().getLanguage(response, properties.getLanguageCode());
            if (language == null) return null; //there is no question to ask
            if (response.getDisplay().toString().length() > 0) {
                // We have a question
                ResponseReceipt receipt = response.getDisplay().getLanguage().get(0).getReceipt().get(0);
//                if (receipt != null) receiptMessage = receipt.getNotratedvalue();
            }
        } catch (NullPointerException e) {
            log.error("Error fetching the next question", e);
        }
        return response;
    }

    private void runQuestion(ITruModuleProperties properties, Response response) {

        String keyStroke = String.valueOf(NO_QUESTION_ASKED);
        long totalTimeTaken = 0;

        try {
            if (response == null) return;
            final ResponseLanguage language =
                    new LanguageManager().getLanguage(response, properties.getLanguageCode());
            final ResponseQuestion question = language.getQuestion();

            final int displayWidth = properties.getDeviceCpl();
            String qText = question.getValue();
            if (qText != null) {
                String[] qTextWraps = qText.split("\\\\n");
                if ((qTextWraps.length == 1) && (qTextWraps[0].length() > displayWidth))
                    qTextWraps = StringUtilities.wordWrap(qText, displayWidth);

                int timeout = properties.getQuestionTimeout();
                if (timeout < 1000)
                    timeout = 60000;

                final long startTime = System.currentTimeMillis();
                keyStroke = iDevice.displayTruratingQuestionGetKeystroke(qTextWraps, qText, timeout);
                final long endTime = System.currentTimeMillis();
                totalTimeTaken = endTime - startTime;
            }

            RequestRating rating;
            int ratingValue = new Integer(keyStroke);
            if (ratingValue >= -1 && ratingValue <= 9) {
                rating = new RequestRating();
                rating.setValue(new Short(keyStroke));
                rating.setResponseTimeMs(new Long(totalTimeTaken).intValue());
                if (ratingValue > -1) {
                    // Update the receipt text to indicate that the user rated
                    String questionText = response.getDisplay().getLanguage().get(0).getQuestion().getValue();
                    iDevice.displayMessage(questionText);

                    final ResponseReceipt receipt = this.response.getDisplay().getLanguage().get(0).getReceipt().get(0);
                    receiptMessage = receipt.getValue();

//                    String prizeCode = prizeManagerService.checkForAPrize(getDevice(), response., properties.getLanguageCode());
//                    if (prizeCode != null) rating.setPrizecode(prizeCode);

                } else {
                    String noRateText = "Sorry you didn't rate";
                    iDevice.displayMessage(noRateText);

                    final ResponseReceipt receipt = language.getReceipt().get(0);
                    receiptMessage = receipt.getValue();
                }
                getCurrentRatingRecord(properties).setValue(rating.getValue());
            }
        } catch (Exception e) {
            log.error("truModule error", e);
        }
    }

    /**
     * The message that should appear on the receipt as a consequence
     * of the outcome of this rating question
     */
    public String getReceiptMessage() {
        return receiptMessage;
    }


    public void close() {
        clearValueOfCachedRatingAndReceipt();
        xmlNetworkMessenger.close();
    }

    public void clearValueOfCachedRatingAndReceipt() {
        // Clear the current transaction
        currentRequest = null;
        receiptMessage = "";
    }

    // Set the device in use
    public void setDevice(IDevice deviceRef) {
        this.iDevice = deviceRef;
    }

    private IDevice getDevice() {
        return iDevice;
    }
}
