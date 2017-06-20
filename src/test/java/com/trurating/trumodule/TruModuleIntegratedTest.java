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
package com.trurating.trumodule;

import com.trurating.service.v220.xml.*;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.device.IReceiptManager;
import com.trurating.trumodule.messages.PosParams;
import com.trurating.trumodule.messages.TruModuleMessageFactory;
import com.trurating.trumodule.mocks.MockDevice;
import com.trurating.trumodule.mocks.MockReceiptManger;
import com.trurating.trumodule.network.HttpClient;
import com.trurating.trumodule.network.ISerializer;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.properties.TruModuleProperties;
import com.trurating.trumodule.util.TruModuleDateUtils;
import com.trurating.trumodule.util.TruModuleUnitTest;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static mockit.Deencapsulation.getField;
import static mockit.Deencapsulation.setField;
import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
@RunWith(JMockit.class)
public class TruModuleIntegratedTest extends TruModuleUnitTest{
    @Tested
    private TruModuleIntegrated truModuleIntegrated;

    private IDevice iDevice;
    private IReceiptManager iReceiptManager;

    @Injectable
    private ISerializer serializer;
    @Mocked
    private HttpClient httpClient;
    @Injectable
    private Logger logger;
    @Injectable
    private ITruModuleProperties properties;

    private String partnerId ="1";
    private String merchantId = "UNITTEST_MERCH_1";
    private String terminalId = "UNITTEST_TERM_1";
    private String sessionId = "UNITTEST_SESSION_1";

    @Before
    public void setUp() throws Exception {
        properties = new TruModuleProperties(partnerId,merchantId,terminalId);
        iDevice = new MockDevice();
        iReceiptManager = new MockReceiptManger();
        truModuleIntegrated = new TruModuleIntegrated(properties,iReceiptManager,iDevice,serializer,true);
        setField(truModuleIntegrated,"isActivated",true);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        setField(truModuleIntegrated, "httpClient", httpClient);
        setField(truModuleIntegrated, "logger", logger);
    }

    @Test
    public void sendPosEvent() throws Exception {
        final PosParams params = new PosParams(sessionId);
        RequestPosStartTilling requestPosStartTilling = new RequestPosStartTilling();
        final RequestPosEvent requestPosEvent = new RequestPosEvent();
        requestPosEvent.setStartTilling(requestPosStartTilling);

        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);

        new Expectations() {{
            truModuleIntegrated.sendPosEventImpl(params,requestPosEvent);
        }};

        // Quick hack to stop sendPosEvent attempting to send any data
        setField(truModuleIntegrated,"isActivated",false);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() - TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        truModuleIntegrated.sendPosEvent(params,requestPosEvent);
        Thread.sleep(200);
        setField(truModuleIntegrated,"isActivated",true);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
    }


    @Test
    public void sendPosEventImpl() throws Exception {
        PosParams params = new PosParams(sessionId);
        RequestPosStartTilling requestPosStartTilling = new RequestPosStartTilling();
        RequestPosEvent requestPosEvent = new RequestPosEvent();
        requestPosEvent.setStartTilling(requestPosStartTilling);

        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(response);
        }};

        truModuleIntegrated.sendPosEventImpl(params,requestPosEvent);

        new Verifications() {{
            truModuleIntegrated.isActivated();
        }};
    }

    @Test
    public void sendPosEventImpl_clearResponse() throws Exception {
        PosParams params = new PosParams(sessionId);
        RequestPosStartTilling requestPosStartTilling = new RequestPosStartTilling();
        RequestPosEvent requestPosEvent = new RequestPosEvent();
        requestPosEvent.setStartTilling(requestPosStartTilling);

        ResponseEventClear responseEventClear = new ResponseEventClear();
        ResponseEvent responseEvent = new ResponseEvent();
        responseEvent.setClear(responseEventClear);
        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);
        response.setEvent(responseEvent);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(response);
        }};

        setField(truModuleIntegrated,"questionRunning",true);
        setField(truModuleIntegrated,"questionCancelled",false);
        truModuleIntegrated.sendPosEventImpl(params,requestPosEvent);
        Assert.assertTrue(truModuleIntegrated.isQuestionCancelled());

        new FullVerificationsInOrder() {{
            truModuleIntegrated.isActivated();
            truModuleIntegrated.cancelRating();
            iDevice.resetDisplay();
        }};
    }

    @Test
    public void sendPosEventImpl_questionTriggerResponse() throws Exception {
        PosParams params = new PosParams(sessionId);
        RequestPosStartTilling requestPosStartTilling = new RequestPosStartTilling();
        RequestPosEvent requestPosEvent = new RequestPosEvent();
        requestPosEvent.setStartTilling(requestPosStartTilling);

        ResponseEventQuestion responseEventQuestion = new ResponseEventQuestion();
        responseEventQuestion.setTrigger(Trigger.DWELLTIME);
        ResponseEvent responseEvent = new ResponseEvent();
        responseEvent.setQuestion(responseEventQuestion);
        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);
        response.setEvent(responseEvent);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(response);
            returns(null);
        }};

        truModuleIntegrated.sendPosEventImpl(params,requestPosEvent);

        new Verifications() {{
            truModuleIntegrated.isActivated();
            truModuleIntegrated.doRating((Request)any);
        }};
    }

    @Test
    public void sendPosEventList() throws Exception {
        final PosParams params = new PosParams(sessionId);
        RequestPosStartTilling requestPosStartTilling = new RequestPosStartTilling();
        final RequestPosEventList requestPosEventList = new RequestPosEventList();
        requestPosEventList.setStartTilling(requestPosStartTilling);

        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);

        new Expectations() {{
            truModuleIntegrated.sendPosEventListImpl(params,requestPosEventList);
        }};

        // Quick hack to stop sendPosEventList attempting to send any data
        setField(truModuleIntegrated,"isActivated",false);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() - TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        truModuleIntegrated.sendPosEventList(params,requestPosEventList);
        Thread.sleep(200);
        setField(truModuleIntegrated,"isActivated",true);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
    }


    @Test
    public void sendPosEventListImpl() throws Exception {
        PosParams params = new PosParams(sessionId);
        RequestPosStartTilling requestPosStartTilling = new RequestPosStartTilling();
        RequestPosEventList requestPosEventList = new RequestPosEventList();
        requestPosEventList.setStartTilling(requestPosStartTilling);

        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(response);
        }};

        truModuleIntegrated.sendPosEventListImpl(params,requestPosEventList);

        new Verifications() {{
            truModuleIntegrated.isActivated();
        }};
    }

    @Test
    public void sendPosEventListImpl_clearResponse() throws Exception {
        PosParams params = new PosParams(sessionId);
        RequestPosStartTilling requestPosStartTilling = new RequestPosStartTilling();
        RequestPosEventList requestPosEventList = new RequestPosEventList();
        requestPosEventList.setStartTilling(requestPosStartTilling);

        ResponseEventClear responseEventClear = new ResponseEventClear();
        ResponseEvent responseEvent = new ResponseEvent();
        responseEvent.setClear(responseEventClear);
        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);
        response.setEvent(responseEvent);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(response);
        }};

        setField(truModuleIntegrated,"questionRunning",true);
        setField(truModuleIntegrated,"questionCancelled",false);
        truModuleIntegrated.sendPosEventListImpl(params,requestPosEventList);
        Assert.assertTrue(truModuleIntegrated.isQuestionCancelled());

        new FullVerificationsInOrder() {{
            truModuleIntegrated.isActivated();
            truModuleIntegrated.cancelRating();
            iDevice.resetDisplay();
        }};
    }

    @Test
    public void sendPosEventListImpl_questionTriggerResponse() throws Exception {
        PosParams params = new PosParams(sessionId);
        RequestPosStartTilling requestPosStartTilling = new RequestPosStartTilling();
        RequestPosEventList requestPosEventList = new RequestPosEventList();
        requestPosEventList.setStartTilling(requestPosStartTilling);

        ResponseEventQuestion responseEventQuestion = new ResponseEventQuestion();
        responseEventQuestion.setTrigger(Trigger.DWELLTIME);
        ResponseEvent responseEvent = new ResponseEvent();
        responseEvent.setQuestion(responseEventQuestion);
        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);
        response.setEvent(responseEvent);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(response);
            returns(null);
        }};

        truModuleIntegrated.sendPosEventListImpl(params,requestPosEventList);

        new Verifications() {{
            truModuleIntegrated.isActivated();
            truModuleIntegrated.doRating((Request)any);
        }};
    }

    @Test
    public void sendTransaction() throws Exception {
        final PosParams params = new PosParams(sessionId);

        final RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setType("Payment");
        requestTransaction.setResult(TransactionResult.APPROVED);
        requestTransaction.setId(sessionId);
        requestTransaction.setGratuity(0);
        requestTransaction.setCurrency((short)356);
        requestTransaction.setAmount(98723);


        new Expectations() {{
            truModuleIntegrated.sendTransactionImpl(params,requestTransaction);
        }};

        // Quick hack to stop sendTransaction attempting to send any data
        setField(truModuleIntegrated,"isActivated",false);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() - TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        truModuleIntegrated.sendTransaction(params,requestTransaction);
        Thread.sleep(200);
        setField(truModuleIntegrated,"isActivated",true);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
    }


    @Test
    public void sendTransactionImpl() throws Exception {
        final int amount = 98723;
        final short currency = 356;

        final PosParams params = new PosParams(sessionId);

        final RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setType("Payment");
        requestTransaction.setResult(TransactionResult.APPROVED);
        requestTransaction.setId(sessionId);
        requestTransaction.setGratuity(0);
        requestTransaction.setCurrency(currency);
        requestTransaction.setAmount(amount);

        truModuleIntegrated.sendTransactionImpl(params,requestTransaction);

        new Verifications() {{
            Request requestTransaction;
            truModuleIntegrated.isActivated();
            httpClient.send((URL)any,requestTransaction = withCapture());
            assertEquals(amount,requestTransaction.getTransaction().getAmount());
            assertEquals(currency,requestTransaction.getTransaction().getCurrency());
            assertEquals(TransactionResult.APPROVED,requestTransaction.getTransaction().getResult());
        }};
    }

    @Test
    public void sendTransaction1() throws Exception {
        final int amount = 98723;
        final short currency = 356;

        final RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setType("Payment");
        requestTransaction.setResult(TransactionResult.APPROVED);
        requestTransaction.setId(sessionId);
        requestTransaction.setGratuity(0);
        requestTransaction.setCurrency(currency);
        requestTransaction.setAmount(amount);

        truModuleIntegrated.sendTransaction(sessionId,requestTransaction);

        new Verifications() {{
            Request requestTransaction;
            httpClient.send((URL)any,requestTransaction = withCapture());
            assertEquals(amount,requestTransaction.getTransaction().getAmount());
            assertEquals(currency,requestTransaction.getTransaction().getCurrency());
            assertEquals(TransactionResult.APPROVED,requestTransaction.getTransaction().getResult());
        }};
    }

    @Test
    public void initiatePayment_clearQuestion() throws Exception {
        PosParams params = new PosParams(sessionId);

        setField(truModuleIntegrated,"questionRunning",true);
        setField(truModuleIntegrated,"questionCancelled",false);
        truModuleIntegrated.setTrigger(Trigger.DWELLTIME);
        truModuleIntegrated.initiatePayment(params);
        Assert.assertTrue(truModuleIntegrated.isQuestionCancelled());

        new VerificationsInOrder() {{
            truModuleIntegrated.isActivated();
            truModuleIntegrated.cancelRating();
            iDevice.resetDisplay();
        }};
    }

    @Test
    public void initiatePayment_triggerQuestion() throws Exception {
        PosParams params = new PosParams(sessionId);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(null);
        }};

        truModuleIntegrated.setTrigger(Trigger.PAYMENTREQUEST);
        truModuleIntegrated.initiatePayment(params);

        new Verifications() {{
            truModuleIntegrated.isActivated();
            truModuleIntegrated.doRating((Request)any);
        }};
    }

    @Test
    public void getLogger() throws Exception {
        Assert.assertEquals(logger,truModuleIntegrated.getLogger());
    }

    @Test
    public void getTruModuleProperties() throws Exception {
        Assert.assertEquals(properties,truModuleIntegrated.getTruModuleProperties());
    }

    @Test
    public void getIDevice() throws Exception {
        Assert.assertEquals(iDevice,truModuleIntegrated.getIDevice());
    }

    @Test
    public void setIDevice() throws Exception {
        truModuleIntegrated.setIDevice(null);
        Assert.assertNull(testGetValue(truModuleIntegrated,"iDevice"));
        setField(truModuleIntegrated,"iDevice",iDevice);
    }

    @Test
    public void getIReceiptManager() throws Exception {
        Assert.assertEquals(iReceiptManager,truModuleIntegrated.getIReceiptManager());
    }

    @Test
    public void setIReceiptManager() throws Exception {
        truModuleIntegrated.setIReceiptManager(null);
        Assert.assertNull(testGetValue(truModuleIntegrated,"iReceiptManager"));
        setField(truModuleIntegrated,"iReceiptManager",iReceiptManager);
    }

    @Test
    public void getSessionId() throws Exception {
        testSetValue(truModuleIntegrated,"sessionId","TESTID_1");
        Assert.assertEquals("TESTID_1",truModuleIntegrated.getSessionId());
    }

    @Test
    public void getSessionId1() throws Exception {
        PosParams params = new PosParams();
        params.setSessionId("TESTID_2");
        Assert.assertEquals("TESTID_2",truModuleIntegrated.getSessionId(params));
    }

    @Test
    public void setSessionId() throws Exception {
        truModuleIntegrated.setSessionId("TESTID_2");
        Assert.assertEquals("TESTID_2",testGetValue(truModuleIntegrated,"sessionId"));
        setField(truModuleIntegrated,"sessionId",null);
    }

    @Test
    public void getTrigger() throws Exception {
        testSetValue(truModuleIntegrated,"trigger",Trigger.AMOUNTFINALIZED);
        Assert.assertEquals(Trigger.AMOUNTFINALIZED,truModuleIntegrated.getTrigger());
    }

    @Test
    public void setTrigger() throws Exception {
        truModuleIntegrated.setTrigger(Trigger.PREAPPROVAL);
        Assert.assertEquals(Trigger.PREAPPROVAL,testGetValue(truModuleIntegrated,"trigger"));
    }

    @Test
    public void cancelRating() throws Exception {
        setField(truModuleIntegrated,"questionRunning",true);
        setField(truModuleIntegrated,"questionCancelled",false);
        truModuleIntegrated.cancelRating();
        Assert.assertTrue(truModuleIntegrated.isQuestionCancelled());

        new VerificationsInOrder() {{
            truModuleIntegrated.cancelRating();
            iDevice.resetDisplay();
        }};
    }

    @Test
    public void activate() throws Exception {
        final Response sectorLookup = new Response();
        ResponseLookup responseSectorLookup = new ResponseLookup();
        ResponseLookup.Language language = new ResponseLookup.Language();
        language.setRfc1766("en-GB");
        List<LookupOption> sectorOptions = new LinkedList<LookupOption>();
        LookupOption sectorOption = new LookupOption();
        sectorOption.setText("SECTOR_TEXT_0");
        sectorOption.setValue("100");
        sectorOptions.add(0,sectorOption);
        sectorOption = new LookupOption();
        sectorOption.setText("SECTOR_TEXT_1");
        sectorOption.setValue("101");
        sectorOptions.add(1,sectorOption);
        sectorOption = new LookupOption();
        sectorOption.setText("SECTOR_TEXT_2");
        sectorOption.setValue("102");
        sectorOptions.add(2,sectorOption);
        sectorOption = new LookupOption();
        sectorOption.setText("SECTOR_TEXT_3");
        sectorOption.setValue("103");
        sectorOptions.add(3,sectorOption);
        sectorOption = new LookupOption();
        sectorOption.setText("SECTOR_TEXT_4");
        sectorOption.setValue("104");
        sectorOptions.add(4,sectorOption);
        testSetValue(language,"option",sectorOptions);
        responseSectorLookup.getLanguage().add(language);
        sectorLookup.setLookup(responseSectorLookup);

        final Response timezoneLookup = new Response();
        ResponseLookup responseTimezoneOptionLookup = new ResponseLookup();
        ResponseLookup.Language timezoneLanguage = new ResponseLookup.Language();
        timezoneLanguage.setRfc1766("en-GB");
        List<LookupOption> timezoneOptions = new LinkedList<LookupOption>();
        LookupOption timezoneOption = new LookupOption();
        timezoneOption.setText("TIMEZONE_TEXT_0");
        timezoneOption.setValue("10");
        timezoneOptions.add(0,timezoneOption);
        timezoneOption = new LookupOption();
        timezoneOption.setText("TIMEZONE_TEXT_1");
        timezoneOption.setValue("20");
        timezoneOptions.add(1,timezoneOption);
        timezoneOption = new LookupOption();
        timezoneOption.setText("TIMEZONE_TEXT_2");
        timezoneOption.setValue("30");
        timezoneOptions.add(2,timezoneOption);
        timezoneOption = new LookupOption();
        timezoneOption.setText("TIMEZONE_TEXT_3");
        timezoneOption.setValue("40");
        timezoneOptions.add(3,timezoneOption);
        timezoneOption = new LookupOption();
        timezoneOption.setText("TIMEZONE_TEXT_4");
        timezoneOption.setValue("50");
        timezoneOptions.add(4,timezoneOption);
        testSetValue(timezoneLanguage,"option",timezoneOptions);
        responseTimezoneOptionLookup.getLanguage().add(timezoneLanguage);
        timezoneLookup.setLookup(responseTimezoneOptionLookup);

        final Response activateResponse = new Response();
        activateResponse.setPartnerId(partnerId);
        activateResponse.setMerchantId(merchantId);
        activateResponse.setTerminalId(terminalId);
        activateResponse.setSessionId(sessionId);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setIsActive(true);
        responseStatus.setTimeToLive(1000);
        activateResponse.setStatus(responseStatus);
        activateResponse.setStatus(responseStatus);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(sectorLookup);
            returns(timezoneLookup);
            returns(activateResponse);
        }};

        setField(truModuleIntegrated,"isActivated",false);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow

        truModuleIntegrated.activate();
        Assert.assertTrue((Boolean) getField(truModuleIntegrated,"isActivated"));
    }

    @Test
    public void activate1() throws Exception {
        final Response activateResponse = new Response();
        activateResponse.setPartnerId(partnerId);
        activateResponse.setMerchantId(merchantId);
        activateResponse.setTerminalId(terminalId);
        activateResponse.setSessionId(sessionId);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setIsActive(true);
        responseStatus.setTimeToLive(1000);
        activateResponse.setStatus(responseStatus);
        activateResponse.setStatus(responseStatus);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(activateResponse);
        }};

        setField(truModuleIntegrated,"isActivated",false);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow

        truModuleIntegrated.activate("UNIT_TEST_REGCODE");
        Assert.assertTrue((Boolean) getField(truModuleIntegrated,"isActivated"));
    }

    @Test
    public void activate2() throws Exception {
        final Response activateResponse = new Response();
        activateResponse.setPartnerId(partnerId);
        activateResponse.setMerchantId(merchantId);
        activateResponse.setTerminalId(terminalId);
        activateResponse.setSessionId(sessionId);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setIsActive(true);
        responseStatus.setTimeToLive(1000);
        activateResponse.setStatus(responseStatus);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(activateResponse);
        }};

        setField(truModuleIntegrated,"isActivated",false);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow

        truModuleIntegrated.activate(4,"GMT",PaymentInstant.PAYAFTER,"unittest@trurating.com","password","107 Leadenhall Street","07765123456","Unit test merchants","Unit test business");
        Assert.assertTrue((Boolean) getField(truModuleIntegrated,"isActivated"));
    }

    @Test
    public void isActivated() throws Exception {
        setField(truModuleIntegrated,"isActivated",true);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        Assert.assertTrue(truModuleIntegrated.isActivated());
    }

    @Test
    public void isActivated_false() throws Exception {
        setField(truModuleIntegrated,"isActivated",false);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        Assert.assertFalse(truModuleIntegrated.isActivated());
    }

    @Test
    public void isActivated_recheck() throws Exception {
        setField(truModuleIntegrated,"isActivated",false);
        setField(truModuleIntegrated,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() - TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow

        final Response activateResponse = new Response();
        activateResponse.setPartnerId(partnerId);
        activateResponse.setMerchantId(merchantId);
        activateResponse.setTerminalId(terminalId);
        activateResponse.setSessionId(sessionId);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setIsActive(true);
        responseStatus.setTimeToLive(1000);
        activateResponse.setStatus(responseStatus);
        activateResponse.setStatus(responseStatus);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(activateResponse);
        }};

        Assert.assertTrue(truModuleIntegrated.isActivated());
    }


    @Test
    public void sendRequest() throws Exception {
        RequestPosStartTilling requestPosStartTilling = new RequestPosStartTilling();
        RequestPosEvent requestPosEvent = new RequestPosEvent();
        requestPosEvent.setStartTilling(requestPosStartTilling);
        final Request request = new Request();
        request.setPartnerId(partnerId);
        request.setMerchantId(merchantId);
        request.setTerminalId(terminalId);
        request.setSessionId(sessionId);
        request.setPosEvent(requestPosEvent);

        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);

        new Expectations() {{
            httpClient.send((URL)any,(Request)any);
            returns(response);
        }};

        truModuleIntegrated.sendRequest(request);

        new Verifications() {{
            Request captureReq;
            httpClient.send((URL)any,captureReq = withCapture());
            assertEquals(request,captureReq);
        }};
    }

    @Test
    public void doRating() throws Exception {
        final Request request = TruModuleMessageFactory.assembleQuestionRequest(
                iDevice.getCurrentLanguage(),
                iDevice,
                iReceiptManager,
                partnerId,
                merchantId,
                terminalId,
                sessionId,
                Trigger.DWELLTIME);

        RequestRating requestRating = new RequestRating();
        requestRating.setValue((short)4);
        request.setRating(requestRating);

        final Response response = new Response();
        response.setPartnerId(partnerId);
        response.setMerchantId(merchantId);
        response.setTerminalId(terminalId);
        response.setSessionId(sessionId);

        new Expectations() {{
            //noinspection ConstantConditions
            httpClient.send((URL) any, (Request) any);
            times = 2; // there will be two calls to httpClient
            returns(generateResponseForQuestion());
            returns(response);

//
//            //noinspection ConstantConditions
            iDevice.display1AQ1KR(anyString, anyInt);
            times = 1;
//            //noinspection ConstantConditions
            iDevice.displayMessage(anyString);
            times = 2;
        }};

        truModuleIntegrated.doRating(request);

        new VerificationsInOrder() {{
            Request captureReq;
            httpClient.send((URL) any, (Request) any);
            httpClient.send((URL)any,captureReq = withCapture());
            assertEquals(request.getRating().getValue(),captureReq.getRating().getValue());
        }};
    }

    private Response generateResponseForQuestion() {
        Response response = new Response();
        response.setMerchantId("2");
        response.setPartnerId(properties.getPartnerId());
        response.setTerminalId("3");
        ResponseEvent responseEvent = new ResponseEvent();
        ResponseEventQuestion responseEventQuestion = new ResponseEventQuestion();
        responseEventQuestion.setTrigger(Trigger.DWELLTIME);
        responseEvent.setQuestion(responseEventQuestion);
        response.setEvent(responseEvent);

        response.setSessionId(TruModuleDateUtils.getInstance().timeNow().toString());

        ResponseDisplay responseDisplay = new ResponseDisplay();
        ResponseLanguage responseLanguage  = new ResponseLanguage();
        responseLanguage.setRfc1766("en-GB");
        ResponseQuestion responseQuestion = new ResponseQuestion();
        responseQuestion.setMax((short)1);
        responseQuestion.setMin((short)20);
        responseQuestion.setTimeoutMs(5000);
        responseQuestion.setValue("Please rate 0-9");

        ResponseScreen responseScreenNoRated = new ResponseScreen();
        responseScreenNoRated.setTimeoutMs(5000);
        responseScreenNoRated.setValue("Sorry you didn't rate");
        responseScreenNoRated.setWhen(When.NOTRATED);
        responseLanguage.getScreen().add(responseScreenNoRated);

        ResponseScreen responseScreenRated = new ResponseScreen();
        responseScreenRated.setTimeoutMs(5000);
        responseScreenRated.setValue("Thanks for rating");
        responseScreenRated.setWhen(When.RATED);
        responseLanguage.getScreen().add(responseScreenRated);

        responseLanguage.setQuestion(responseQuestion);

        ResponseReceipt responseReceiptRated = new ResponseReceipt();
        responseReceiptRated.setWhen(When.RATED);
        responseReceiptRated.setValue("Thanks for rating");
        ResponseReceipt responseReceiptNotRated = new ResponseReceipt();
        responseReceiptNotRated.setWhen(When.NOTRATED);
        responseReceiptNotRated.setValue("Sorry you didn't rate");
        responseLanguage.getReceipt().add(responseReceiptRated);
        responseLanguage.getReceipt().add(responseReceiptNotRated);

        responseDisplay.getLanguage().add(responseLanguage);

        /*second language*/

        ResponseLanguage responseLanguage2 = new ResponseLanguage();
        responseLanguage2.setRfc1766("es-mx");
        ResponseQuestion responseQuestion2 = new ResponseQuestion();
        responseQuestion2.setMax((short)1);
        responseQuestion2.setMin((short)20);
        responseQuestion2.setTimeoutMs(5000);
        responseQuestion2.setValue("Por favor, valora 0-9");

        ResponseScreen responseScreenNoRated2 = new ResponseScreen();
        responseScreenNoRated2.setTimeoutMs(5000);
        responseScreenNoRated2.setValue("Lo siento no rate-screen");
        responseScreenNoRated2.setWhen(When.NOTRATED);
        responseLanguage2.getScreen().add(responseScreenNoRated2);

        ResponseScreen responseScreenRated2 = new ResponseScreen();
        responseScreenRated2.setTimeoutMs(5000);
        responseScreenRated2.setValue("Gracias para rating-screen");
        responseScreenRated2.setWhen(When.RATED);
        responseLanguage2.getScreen().add(responseScreenRated2);

        responseLanguage2.setQuestion(responseQuestion2);

        ResponseReceipt responseReceiptRated2 = new ResponseReceipt();
        responseReceiptRated2.setWhen(When.NOTRATED);
        responseReceiptRated2.setValue("Lo siento no rate-receipt");
        ResponseReceipt responseReceiptNotRated2 = new ResponseReceipt();
        responseReceiptNotRated2.setWhen(When.RATED);
        responseReceiptNotRated2.setValue("Gracias para rating-receipt");

        responseLanguage2.getReceipt().add(responseReceiptRated2);
        responseLanguage2.getReceipt().add(responseReceiptNotRated2);

        responseDisplay.getLanguage().add(responseLanguage2);
        response.setDisplay(responseDisplay);

        return response;
    }

}