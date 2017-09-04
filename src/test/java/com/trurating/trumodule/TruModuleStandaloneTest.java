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

import com.trurating.service.v230.xml.*;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.device.IReceiptManager;
import com.trurating.trumodule.messages.PosParams;
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
public class TruModuleStandaloneTest extends TruModuleUnitTest{
    @Tested
    private TruModuleStandalone truModuleStandalone;

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
        truModuleStandalone = new TruModuleStandalone(properties,iReceiptManager,iDevice,serializer,true);
        setField(truModuleStandalone,"isActivated",true);
        setField(truModuleStandalone,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        setField(truModuleStandalone, "httpClient", httpClient);
        setField(truModuleStandalone, "logger", logger);
    }

    @Test
    public void sendTransaction() throws Exception {
        final RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setType("Payment");
        requestTransaction.setResult(TransactionResult.APPROVED);
        requestTransaction.setId(sessionId);
        requestTransaction.setGratuity(0);
        requestTransaction.setCurrency((short)356);
        requestTransaction.setAmount(98723);


        new Expectations() {{
            truModuleStandalone.sendTransactionImpl(requestTransaction);
        }};

        // Quick hack to stop sendTransaction attempting to send any data
        setField(truModuleStandalone,"isActivated",false);
        setField(truModuleStandalone,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() - TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        truModuleStandalone.sendTransaction(requestTransaction);
        Thread.sleep(200);
        setField(truModuleStandalone,"isActivated",true);
        setField(truModuleStandalone,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
    }


    @Test
    public void sendTransactionImpl() throws Exception {
        final int amount = 98723;
        final short currency = 356;

        final RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setType("Payment");
        requestTransaction.setResult(TransactionResult.APPROVED);
        requestTransaction.setId(sessionId);
        requestTransaction.setGratuity(0);
        requestTransaction.setCurrency(currency);
        requestTransaction.setAmount(amount);

        truModuleStandalone.sendTransactionImpl(requestTransaction);

        new Verifications() {{
            Request requestTransaction;
            truModuleStandalone.isActivated();
            httpClient.send((URL)any,requestTransaction = withCapture());
            assertEquals(amount,requestTransaction.getTransaction().getAmount());
            assertEquals(currency,requestTransaction.getTransaction().getCurrency());
            assertEquals(TransactionResult.APPROVED,requestTransaction.getTransaction().getResult());
        }};
    }

    @Test
    public void getLogger() throws Exception {
        Assert.assertEquals(logger,truModuleStandalone.getLogger());
    }

    @Test
    public void getTruModuleProperties() throws Exception {
        Assert.assertEquals(properties,truModuleStandalone.getTruModuleProperties());
    }

    @Test
    public void getIDevice() throws Exception {
        Assert.assertEquals(iDevice,truModuleStandalone.getIDevice());
    }

    @Test
    public void setIDevice() throws Exception {
        truModuleStandalone.setIDevice(null);
        Assert.assertNull(testGetValue(truModuleStandalone,"iDevice"));
        setField(truModuleStandalone,"iDevice",iDevice);
    }

    @Test
    public void getIReceiptManager() throws Exception {
        Assert.assertEquals(iReceiptManager,truModuleStandalone.getIReceiptManager());
    }

    @Test
    public void setIReceiptManager() throws Exception {
        truModuleStandalone.setIReceiptManager(null);
        Assert.assertNull(testGetValue(truModuleStandalone,"iReceiptManager"));
        setField(truModuleStandalone,"iReceiptManager",iReceiptManager);
    }

    @Test
    public void getSessionId() throws Exception {
        testSetValue(truModuleStandalone,"sessionId","TESTID_1");
        Assert.assertEquals("TESTID_1",truModuleStandalone.getSessionId());
    }

    @Test
    public void getSessionId1() throws Exception {
        PosParams params = new PosParams();
        params.setSessionId("TESTID_2");
        Assert.assertEquals("TESTID_2",truModuleStandalone.getSessionId(params));
    }

    @Test
    public void setSessionId() throws Exception {
        truModuleStandalone.setSessionId("TESTID_2");
        Assert.assertEquals("TESTID_2",testGetValue(truModuleStandalone,"sessionId"));
        setField(truModuleStandalone,"sessionId",null);
    }

    @Test
    public void getTrigger() throws Exception {
        testSetValue(truModuleStandalone,"trigger",Trigger.AMOUNTFINALIZED);
        Assert.assertEquals(Trigger.AMOUNTFINALIZED,truModuleStandalone.getTrigger());
    }

    @Test
    public void setTrigger() throws Exception {
        truModuleStandalone.setTrigger(Trigger.PREAPPROVAL);
        Assert.assertEquals(Trigger.PREAPPROVAL,testGetValue(truModuleStandalone,"trigger"));
    }

    @Test
    public void cancelRating() throws Exception {
        setField(truModuleStandalone,"questionRunning",true);
        setField(truModuleStandalone,"questionCancelled",false);
        truModuleStandalone.cancelRating();
        Assert.assertTrue(truModuleStandalone.isQuestionCancelled());

        new VerificationsInOrder() {{
            truModuleStandalone.cancelRating();
            iDevice.resetDisplay();
        }};
    }

    @Test
    public void activate() throws Exception {
        final Response sectorLookup = new Response();
        ResponseLookup responseSectorLookup = new ResponseLookup();
        ResponseLookupLanguage language = new ResponseLookupLanguage();
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
        ResponseLookupLanguage timezoneLanguage = new ResponseLookupLanguage();
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

        setField(truModuleStandalone,"isActivated",false);
        setField(truModuleStandalone,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow

        truModuleStandalone.activate();
        Assert.assertTrue((Boolean) getField(truModuleStandalone,"isActivated"));
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

        setField(truModuleStandalone,"isActivated",false);
        setField(truModuleStandalone,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow

        truModuleStandalone.activate("UNIT_TEST_REGCODE");
        Assert.assertTrue((Boolean) getField(truModuleStandalone,"isActivated"));
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

        setField(truModuleStandalone,"isActivated",false);
        setField(truModuleStandalone,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow

        truModuleStandalone.activate(4,"GMT",PaymentInstant.PAYAFTER,"unittest@trurating.com","password","107 Leadenhall Street","07765123456","Unit test merchants","Unit test business");
        Assert.assertTrue((Boolean) getField(truModuleStandalone,"isActivated"));
    }

    @Test
    public void isActivated() throws Exception {
        setField(truModuleStandalone,"isActivated",true);
        setField(truModuleStandalone,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        Assert.assertTrue(truModuleStandalone.isActivated());
    }

    @Test
    public void isActivated_false() throws Exception {
        setField(truModuleStandalone,"isActivated",false);
        setField(truModuleStandalone,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() + TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow
        Assert.assertFalse(truModuleStandalone.isActivated());
    }

    @Test
    public void isActivated_recheck() throws Exception {
        setField(truModuleStandalone,"isActivated",false);
        setField(truModuleStandalone,"activationRecheck", new AtomicLong(TruModuleDateUtils.getInstance().timeNowMillis() - TimeUnit.DAYS.toMillis(1))); // Set the next activation check to tomorrow

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

        Assert.assertTrue(truModuleStandalone.isActivated());
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

        truModuleStandalone.sendRequest(request);

        new Verifications() {{
            Request captureReq;
            httpClient.send((URL)any,captureReq = withCapture());
            assertEquals(request,captureReq);
        }};
    }

    @Test
    public void doRating() throws Exception {

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

        truModuleStandalone.doRating();

        new VerificationsInOrder() {{
            Request captureReq;
            httpClient.send((URL) any, (Request) any);
            httpClient.send((URL)any,captureReq = withCapture());
            assertEquals(4,captureReq.getRating().getValue());
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