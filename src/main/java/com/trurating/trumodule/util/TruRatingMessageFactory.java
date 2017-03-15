/*
 * // The MIT License
 * //
 * // Copyright (c) 2017 TruRating Ltd. https://www.trurating.com
 * //
 * // Permission is hereby granted, free of charge, to any person obtaining a copy
 * // of this software and associated documentation files (the "Software"), to deal
 * // in the Software without restriction, including without limitation the rights
 * // to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * // copies of the Software, and to permit persons to whom the Software is
 * // furnished to do so, subject to the following conditions:
 * //
 * // The above copyright notice and this permission notice shall be included in
 * // all copies or substantial portions of the Software.
 * //
 * // THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * // IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * // FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * // AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * // LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * // OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * // THE SOFTWARE.
 */

package com.trurating.trumodule.util;

import com.trurating.service.v210.xml.*;
import com.trurating.trumodule.device.IDevice;

import java.util.logging.Logger;

/**
 * The type Tru rating message factory.
 */
@SuppressWarnings("Duplicates")
public class TruRatingMessageFactory {

    private static Logger logger = Logger.getLogger(TruRatingMessageFactory.class.getName());

    /**
     * Assemble request transaction request.
     *
     * @param partnerId          the partner id
     * @param merchantId         the merchant id
     * @param terminalId         the terminal id
     * @param sessionId          the session id
     * @param requestTransaction the request transaction
     * @return the request
     */
    public Request assembleRequestTransaction(String partnerId,
                                              String merchantId,
                                              String terminalId,
                                              String sessionId,
                                              RequestTransaction requestTransaction) {
        try {
            Request request = new Request();
            request.setPartnerId(partnerId);
            request.setMerchantId(merchantId);
            request.setTerminalId(terminalId);
            request.setSessionId(sessionId);
            request.setTransaction(requestTransaction);
            return request;
        }catch(Exception e){
            logger.severe("There was an error assembling the request for a transaction ");
            logger.severe(e.toString());
            return null;
        }
    }

    /**
     * Assemble ratings delivery request request.
     *
     * @param rfc1766        the rfc 1766
     * @param partnerId      the partner id
     * @param merchantId     the merchant id
     * @param terminalId     the terminal id
     * @param sessionId      the session id
     * @param value          the value
     * @param responseTimeMs the response time ms
     * @return the request
     */
    public Request assembleRatingsDeliveryRequest(String rfc1766,
                                                         String partnerId,
                                                         String merchantId,
                                                         String terminalId,
                                                         String sessionId,
                                                         short value,
                                                         int responseTimeMs) {

        try {
            Request request = new Request();
            request.setPartnerId(partnerId);
            request.setMerchantId(merchantId);
            request.setTerminalId(terminalId);
            request.setSessionId(sessionId);

            RequestRating requestRating = new RequestRating();

            requestRating.setRfc1766(rfc1766);
            requestRating.setDateTime(TruModuleDateUtils.timeNow());
            requestRating.setValue(value);
            requestRating.setResponseTimeMs(responseTimeMs);

            request.setRating(requestRating);
            return request;
        } catch (Exception e) {
            System.out.println("There was an error assembling the request for a rating ");
            logger.severe("There was an error assembling the request for a rating ");
            logger.severe(e.toString());
            return null;
        }
    }

    /**
     * Assemble question request request.
     *
     * @param rfc1766    the rfc 1766
     * @param device     the device
     * @param partnerId  the partner id
     * @param merchantId the merchant id
     * @param terminalId the terminal id
     * @param sessionId  the session id
     * @param trigger    the trigger
     * @return the request
     */
    public Request assembleQuestionRequest(String rfc1766,
                                           IDevice device,
                                           String partnerId,
                                           String merchantId,
                                           String terminalId,
                                           String sessionId,
                                           Trigger trigger) {

        try {
            Request request = new Request();
            request.setPartnerId(partnerId);
            request.setMerchantId(merchantId);
            request.setTerminalId(terminalId);
            request.setSessionId(sessionId);

            RequestDevice requestDevice = new RequestDevice();
            requestDevice.setFirmware(device.getFirmware());
            requestDevice.setName(device.getName());

            RequestPeripheral screenPeripheral = new RequestPeripheral();
            screenPeripheral.setFont(device.getScreenCapabilities().getFont());
            screenPeripheral.setFormat(device.getScreenCapabilities().getFormat());
            screenPeripheral.setHeight(device.getScreenCapabilities().getHeight());
            screenPeripheral.setUnit(device.getScreenCapabilities().getUnit());
            screenPeripheral.setSeparator(device.getScreenCapabilities().getSeparator());
            screenPeripheral.setWidth(device.getScreenCapabilities().getWidth());
            requestDevice.setScreen(screenPeripheral);

            if(device.getReceiptCapabilities()!=null) {
                RequestPeripheral receiptPeripheral = new RequestPeripheral();
                receiptPeripheral.setFont(device.getReceiptCapabilities().getFont());
                receiptPeripheral.setFormat(device.getReceiptCapabilities().getFormat());
                receiptPeripheral.setHeight(device.getReceiptCapabilities().getHeight());
                receiptPeripheral.setUnit(device.getReceiptCapabilities().getUnit());
                receiptPeripheral.setSeparator(device.getReceiptCapabilities().getSeparator());
                receiptPeripheral.setWidth(device.getReceiptCapabilities().getWidth());
                requestDevice.setReceipt(receiptPeripheral);
            }

            RequestQuestion requestQuestion = new RequestQuestion();
            requestQuestion.setDevice(requestDevice);

            RequestLanguage requestLanguage = new RequestLanguage();
            requestLanguage.setRfc1766(rfc1766);
            requestQuestion.getLanguage().add(requestLanguage);
            requestQuestion.setTrigger(trigger);
            request.setQuestion(requestQuestion);

            return request;
        } catch (Exception e) {
            logger.severe("There was an error assembling the request for a question ");
            logger.severe(e.toString());
            return null;
        }
    }

    /**
     * Gets test response.
     *
     * @param sessionID the session id
     * @return the test response
     */
    public Response getTestResponse(String sessionID) {
        Response testResponse = new Response();
        ResponseDisplay responseDisplay = new ResponseDisplay();
        ResponseLanguage responseLanguage = new ResponseLanguage();
        ResponseQuestion responseQuestion = new ResponseQuestion();
        responseQuestion.setValue("Please rate 0-9");
        responseLanguage.setQuestion(responseQuestion);
        responseLanguage.setRfc1766("en-GB");
        responseDisplay.getLanguage().add(responseLanguage);
        testResponse.setDisplay(responseDisplay);
        testResponse.setSessionId(sessionID);
        return testResponse;
    }
}
