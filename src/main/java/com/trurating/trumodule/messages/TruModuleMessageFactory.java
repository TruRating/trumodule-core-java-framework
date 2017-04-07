/*
 *
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
 *
 */
package com.trurating.trumodule.messages;

import com.trurating.service.v220.xml.*;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.device.IReceiptManager;
import com.trurating.trumodule.util.TruModuleDateUtils;

import java.util.Arrays;

/**
 * The type Tru module message factory.
 */
public class TruModuleMessageFactory {

    /**
     * Assemble request pos event request.
     *
     * @param partnerId  the partner id
     * @param merchantId the merchant id
     * @param terminalId the terminal id
     * @param sessionId  the session id
     * @param event      the event
     * @return the request
     */
    public static Request assembleRequestPosEvent(String partnerId,
                                                  String merchantId,
                                                  String terminalId,
                                                  String sessionId,
                                                  RequestPosEvent event) {
        Request request = new Request();
        request.setTerminalId(terminalId);
        request.setSessionId(sessionId);
        request.setPartnerId(partnerId);
        request.setMerchantId(merchantId);
        request.setPosEvent(event);
        return request;
    }

    /**
     * Assemble request pos event request.
     *
     * @param partnerId  the partner id
     * @param merchantId the merchant id
     * @param terminalId the terminal id
     * @param sessionId  the session id
     * @param event      the event
     * @return the request
     */
    public static Request assembleRequestPosEvent(String partnerId,
                                                  String merchantId,
                                                  String terminalId,
                                                  String sessionId,
                                                  RequestPosEventList event) {
        Request request = new Request();
        request.setTerminalId(terminalId);
        request.setSessionId(sessionId);
        request.setPartnerId(partnerId);
        request.setMerchantId(merchantId);
        request.setPosEventList(event);
        return request;
    }

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
    public static Request assembleRequestTransaction(String partnerId,
                                                     String merchantId,
                                                     String terminalId,
                                                     String sessionId,
                                                     RequestTransaction requestTransaction) {
        Request request = new Request();
        request.setPartnerId(partnerId);
        request.setMerchantId(merchantId);
        request.setTerminalId(terminalId);
        request.setSessionId(sessionId);
        request.setTransaction(requestTransaction);
        return request;
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
    public static Request assembleRatingsDeliveryRequest(String rfc1766,
                                                         String partnerId,
                                                         String merchantId,
                                                         String terminalId,
                                                         String sessionId,
                                                         short value,
                                                         int responseTimeMs) {

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
    }

    /**
     * Assemble request query request.
     *
     * @param device     the device
     * @param partnerId  the partner id
     * @param merchantId the merchant id
     * @param terminalId the terminal id
     * @param sessionId  the session id
     * @param force      the force
     * @return the request
     */
    public static Request assembleRequestQuery(IReceiptManager receiptManager,
            IDevice device,
                                               String partnerId,
                                               String merchantId,
                                               String terminalId,
                                               String sessionId,
                                               boolean force) {
        Request request = new Request();
        request.setPartnerId(partnerId);
        request.setMerchantId(merchantId);
        request.setTerminalId(terminalId);
        request.setSessionId(sessionId);

        RequestQuery query = new RequestQuery();
        query.setForce(force);

        query.getLanguage().addAll(Arrays.asList(device.getLanguages()));

        RequestDevice requestDevice = new RequestDevice();
        requestDevice.setScreen(device.getScreenCapabilities());
        requestDevice.setReceipt(receiptManager.getReceiptCapabilities());
        requestDevice.setFirmware(device.getFirmware());
        requestDevice.setName(device.getName());
        query.setDevice(requestDevice);

        request.setQuery(query);

        return request;
    }

    /**
     * Assemble request activate request.
     *
     * @param device           the device
     * @param receiptManager   the receipt manager
     * @param partnerId        the partner id
     * @param merchantId       the merchant id
     * @param terminalId       the terminal id
     * @param sessionId        the session id
     * @param registrationCode the registration code
     * @return the request
     */
    public static Request assembleRequestActivate(IDevice device,
                                                  IReceiptManager receiptManager,
                                                  String partnerId,
                                                  String merchantId,
                                                  String terminalId,
                                                  String sessionId,
                                                  String registrationCode) {
        Request request = new Request();
        request.setPartnerId(partnerId);
        request.setMerchantId(merchantId);
        request.setTerminalId(terminalId);
        request.setSessionId(sessionId);

        RequestDevice requestDevice = new RequestDevice();
        requestDevice.setName(device.getName());
        requestDevice.setFirmware(device.getFirmware());
        requestDevice.setScreen(device.getScreenCapabilities());
        requestDevice.setSkipInstruction(device.getSkipInstruction());
        requestDevice.setReceipt(receiptManager.getReceiptCapabilities());

        RequestActivate activate = new RequestActivate();
        activate.setDevice(requestDevice);
        activate.setRegistrationCode(registrationCode);
        activate.getLanguage().addAll(Arrays.asList(device.getLanguages()));
        request.setActivate(activate);

        return request;
    }

    /**
     * Assemble requestlookup request.
     *
     * @param device         the device
     * @param receiptManager the receipt manager
     * @param partnerId      the partner id
     * @param merchantId     the merchant id
     * @param terminalId     the terminal id
     * @param sessionId      the session id
     * @param lookupName     the lookup name
     * @return the request
     */
    public static Request assembleRequestlookup(IDevice device,
                                                IReceiptManager receiptManager,
                                                String partnerId,
                                                String merchantId,
                                                String terminalId,
                                                String sessionId,
                                                LookupName lookupName) {
        Request request = new Request();
        request.setPartnerId(partnerId);
        request.setMerchantId(merchantId);
        request.setTerminalId(terminalId);
        request.setSessionId(sessionId);

        RequestLookup lookup = new RequestLookup();
        lookup.setName(lookupName);
        lookup.getLanguage().addAll(Arrays.asList(device.getLanguages()));

        RequestDevice requestDevice = new RequestDevice();
        requestDevice.setScreen(device.getScreenCapabilities());
        requestDevice.setReceipt(receiptManager.getReceiptCapabilities());
        requestDevice.setFirmware(device.getFirmware());
        requestDevice.setName(device.getName());
        lookup.setDevice(requestDevice);

        request.setLookup(lookup);

        return request;
    }

    /**
     * Assemble request activate request.
     *
     * @param device         the device
     * @param receiptManager the receipt manager
     * @param partnerId      the partner id
     * @param merchantId     the merchant id
     * @param terminalId     the terminal id
     * @param sessionId      the session id
     * @param sectorNode     the sector node
     * @param timeZone       the time zone
     * @param paymentInstant the payment instant
     * @param emailAddress   the email address
     * @param password       the password
     * @param address        the address
     * @param mobileNumber   the mobile number
     * @param merchantName   the merchant name
     * @param businessName   the business name
     * @return the request
     */
    public static Request assembleRequestActivate(IDevice device,
                                                  IReceiptManager receiptManager,
                                                  String partnerId,
                                                  String merchantId,
                                                  String terminalId,
                                                  String sessionId,
                                                  int sectorNode,
                                                  String timeZone,
                                                  PaymentInstant paymentInstant,
                                                  String emailAddress,
                                                  String password,
                                                  String address,
                                                  String mobileNumber,
                                                  String merchantName,
                                                  String businessName) {
        Request request = new Request();
        request.setPartnerId(partnerId);
        request.setMerchantId(merchantId);
        request.setTerminalId(terminalId);
        request.setSessionId(sessionId);

        RequestDevice requestDevice = new RequestDevice();
        requestDevice.setName(device.getName());
        requestDevice.setFirmware(device.getFirmware());
        requestDevice.setScreen(device.getScreenCapabilities());
        requestDevice.setSkipInstruction(device.getSkipInstruction());
        requestDevice.setReceipt(receiptManager.getReceiptCapabilities());

        RequestActivate activate = new RequestActivate();
        activate.setDevice(requestDevice);
        activate.getLanguage().addAll(Arrays.asList(device.getLanguages()));

        RequestRegistrationForm form = new RequestRegistrationForm();
        form.setSectorNode(sectorNode);
        form.setTimeZone(timeZone);
        form.setPaymentInstant(paymentInstant);
        form.setMerchantEmailAddress(emailAddress);
        form.setMerchantPassword(password);
        form.setBusinessAddress(address);
        form.setMerchantMobileNumber(mobileNumber);
        form.setMerchantName(merchantName);
        form.setBusinessName(businessName);

        activate.setRegistrationForm(form);
        request.setActivate(activate);

        return request;
    }

    /**
     * Assemble question request request.
     *
     * @param rfc1766        the rfc 1766
     * @param device         the device
     * @param receiptManager the receipt manager
     * @param partnerId      the partner id
     * @param merchantId     the merchant id
     * @param terminalId     the terminal id
     * @param sessionId      the session id
     * @param trigger        the trigger
     * @return the request
     */
    public static Request assembleQuestionRequest(String rfc1766,
                                                  IDevice device,
                                                  IReceiptManager receiptManager,
                                                  String partnerId,
                                                  String merchantId,
                                                  String terminalId,
                                                  String sessionId,
                                                  Trigger trigger) {

        Request request = new Request();
        request.setPartnerId(partnerId);
        request.setMerchantId(merchantId);
        request.setTerminalId(terminalId);
        request.setSessionId(sessionId);

        RequestDevice requestDevice = new RequestDevice();
        requestDevice.setName(device.getName());
        requestDevice.setFirmware(device.getFirmware());
        requestDevice.setScreen(device.getScreenCapabilities());
        requestDevice.setSkipInstruction(device.getSkipInstruction());
        requestDevice.setReceipt(receiptManager.getReceiptCapabilities());

        RequestQuestion requestQuestion = new RequestQuestion();
        requestQuestion.setDevice(requestDevice);

        RequestLanguage requestLanguage = new RequestLanguage();
        requestLanguage.setRfc1766(rfc1766);
        requestQuestion.getLanguage().add(requestLanguage);
        requestQuestion.setTrigger(trigger);
        request.setQuestion(requestQuestion);

        return request;
    }
}
