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
import com.trurating.trumodule.mocks.MockDevice;
import com.trurating.trumodule.mocks.MockReceiptManger;
import com.trurating.trumodule.util.TruModuleDateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class TruModuleMessageFactoryTest {
    String partnerId ="1";
    String merchantId = "UNITTEST_MERCH_1";
    String terminalId = "UNITTEST_TERM_1";
    String sessionId = "UNITTEST_SESSION_1";
    IDevice device;
    IReceiptManager receiptManager;
    String rfc1766 = "en-GB";

    @Before
    public void setUp() {
        device = new MockDevice();
        receiptManager = new MockReceiptManger();
    }

    @Test
    public void assembleRequestPosEvent() throws Exception {
        RequestPosEvent event = new RequestPosEvent();
        RequestPosStartTilling posStartTilling = new RequestPosStartTilling();
        event.setStartTilling(posStartTilling);

        Request request = TruModuleMessageFactory.assembleRequestPosEvent(partnerId,merchantId,terminalId,sessionId,event);

        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(event,request.getPosEvent());
    }

    @Test
    public void assembleRequestPosEvent1() throws Exception {
        RequestPosEventList event = new RequestPosEventList();
        RequestPosStartTilling posStartTilling = new RequestPosStartTilling();
        event.setStartTilling(posStartTilling);

        Request request = TruModuleMessageFactory.assembleRequestPosEvent(partnerId,merchantId,terminalId,sessionId,event);

        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(event,request.getPosEventList());
    }

    @Test
    public void assembleRequestTransaction() throws Exception {
        RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setAmount(1000);
        requestTransaction.setCurrency((short)826);
        requestTransaction.setDateTime(TruModuleDateUtils.timeFromMillis(1000000));
        requestTransaction.setGratuity(0);
        requestTransaction.setId("UNITTEST_TRANSACTION_1");
        requestTransaction.setResult(TransactionResult.APPROVED);
        requestTransaction.setType("Payment");

        Request request = TruModuleMessageFactory.assembleRequestTransaction(partnerId,merchantId,terminalId,sessionId,requestTransaction);

        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(requestTransaction,request.getTransaction());
    }

    @Test
    public void assembleRatingsDeliveryRequest() throws Exception {
        short value = 5;
        int responseTimeMs = 1500;

        Request request = TruModuleMessageFactory.assembleRatingsDeliveryRequest(rfc1766,partnerId,merchantId,terminalId,sessionId,value,responseTimeMs);

        Assert.assertEquals(rfc1766,request.getRating().getRfc1766());
        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(value,request.getRating().getValue());
        Assert.assertEquals(responseTimeMs,request.getRating().getResponseTimeMs());
    }

    @Test
    public void assembleRequestQuery() throws Exception {
        boolean force = true;

        Request request = TruModuleMessageFactory.assembleRequestQuery(receiptManager,device,partnerId,merchantId,terminalId,sessionId,force);

        Assert.assertEquals(receiptManager.getReceiptCapabilities().getHeight(),request.getQuery().getDevice().getReceipt().getHeight());
        Assert.assertEquals(receiptManager.getReceiptCapabilities().getWidth(),request.getQuery().getDevice().getReceipt().getWidth());
        Assert.assertEquals(device.getScreenCapabilities().getHeight(),request.getQuery().getDevice().getScreen().getHeight());
        Assert.assertEquals(device.getScreenCapabilities().getWidth(),request.getQuery().getDevice().getScreen().getWidth());
        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(force,request.getQuery().isForce());
    }

    @Test
    public void assembleRequestActivate() throws Exception {
        String registrationCode = "UNITTEST_REGCODE_1";

        Request request = TruModuleMessageFactory.assembleRequestActivate(device,receiptManager,partnerId,merchantId,terminalId,sessionId,registrationCode);

        Assert.assertEquals(device.getName(),request.getActivate().getDevice().getName());
        Assert.assertEquals(device.getScreenCapabilities().getHeight(),request.getActivate().getDevice().getScreen().getHeight());
        Assert.assertEquals(device.getScreenCapabilities().getWidth(),request.getActivate().getDevice().getScreen().getWidth());
        Assert.assertEquals(device.getScreenCapabilities().getSeparator(),request.getActivate().getDevice().getScreen().getSeparator());
        Assert.assertEquals(receiptManager.getReceiptCapabilities().getHeight(),request.getActivate().getDevice().getReceipt().getHeight());
        Assert.assertEquals(receiptManager.getReceiptCapabilities().getWidth(),request.getActivate().getDevice().getReceipt().getWidth());
        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(registrationCode,request.getActivate().getRegistrationCode());
    }

    @Test
    public void assembleRequestlookup() throws Exception {
        LookupName lookupName = LookupName.SECTORNODE;

        Request request = TruModuleMessageFactory.assembleRequestlookup(device,receiptManager,partnerId,merchantId,terminalId,sessionId,lookupName);

        Assert.assertEquals(Arrays.asList(device.getLanguages()).get(0).getRfc1766(),request.getLookup().getLanguage().get(0).getRfc1766());
        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(lookupName,request.getLookup().getName());
    }

    @Test
    public void assembleRequestActivate1() throws Exception {
        int sectorNode = 1;
        String timeZone = "GMT";
        PaymentInstant paymentInstant = PaymentInstant.PAYAFTER;
        String emailAddress = "untitestuser@trurating.com";
        String password = "password";
        String address = "107 Leadenhall Street";
        String mobileNumber = "07765 234567";
        String merchantName = "TruRating UnitTest";
        String businessName = "TruRating UnitTest Business";

        Request request = TruModuleMessageFactory.assembleRequestActivate(device,receiptManager,partnerId,merchantId,terminalId,sessionId,sectorNode,timeZone,paymentInstant,emailAddress,password,address,mobileNumber,merchantName,businessName);

        Assert.assertEquals(device.getName(),request.getActivate().getDevice().getName());
        Assert.assertEquals(device.getScreenCapabilities().getHeight(),request.getActivate().getDevice().getScreen().getHeight());
        Assert.assertEquals(device.getScreenCapabilities().getWidth(),request.getActivate().getDevice().getScreen().getWidth());
        Assert.assertEquals(device.getScreenCapabilities().getSeparator(),request.getActivate().getDevice().getScreen().getSeparator());
        Assert.assertEquals(receiptManager.getReceiptCapabilities().getHeight(),request.getActivate().getDevice().getReceipt().getHeight());
        Assert.assertEquals(receiptManager.getReceiptCapabilities().getWidth(),request.getActivate().getDevice().getReceipt().getWidth());
        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(sectorNode,request.getActivate().getRegistrationForm().getSectorNode());
        Assert.assertEquals(timeZone,request.getActivate().getRegistrationForm().getTimeZone());
        Assert.assertEquals(paymentInstant,request.getActivate().getRegistrationForm().getPaymentInstant());
        Assert.assertEquals(emailAddress,request.getActivate().getRegistrationForm().getMerchantEmailAddress());
        Assert.assertEquals(password,request.getActivate().getRegistrationForm().getMerchantPassword());
        Assert.assertEquals(address,request.getActivate().getRegistrationForm().getBusinessAddress());
        Assert.assertEquals(mobileNumber,request.getActivate().getRegistrationForm().getMerchantMobileNumber());
        Assert.assertEquals(merchantName,request.getActivate().getRegistrationForm().getMerchantName());
        Assert.assertEquals(businessName,request.getActivate().getRegistrationForm().getBusinessName());
    }

    @Test
    public void assembleQuestionRequest() throws Exception {
        Trigger trigger = Trigger.AMOUNTFINALIZED;

        Request request = TruModuleMessageFactory.assembleQuestionRequest(rfc1766,device,receiptManager,partnerId,merchantId,terminalId,sessionId,trigger);

        Assert.assertEquals(rfc1766,request.getQuestion().getLanguage().get(0).getRfc1766());
        Assert.assertEquals(device.getName(),request.getQuestion().getDevice().getName());
        Assert.assertEquals(device.getScreenCapabilities().getHeight(),request.getQuestion().getDevice().getScreen().getHeight());
        Assert.assertEquals(device.getScreenCapabilities().getWidth(),request.getQuestion().getDevice().getScreen().getWidth());
        Assert.assertEquals(device.getScreenCapabilities().getSeparator(),request.getQuestion().getDevice().getScreen().getSeparator());
        Assert.assertEquals(receiptManager.getReceiptCapabilities().getHeight(),request.getQuestion().getDevice().getReceipt().getHeight());
        Assert.assertEquals(receiptManager.getReceiptCapabilities().getWidth(),request.getQuestion().getDevice().getReceipt().getWidth());
        Assert.assertEquals(partnerId,request.getPartnerId());
        Assert.assertEquals(merchantId,request.getMerchantId());
        Assert.assertEquals(terminalId,request.getTerminalId());
        Assert.assertEquals(sessionId,request.getSessionId());
        Assert.assertEquals(trigger,request.getQuestion().getTrigger());
    }

}