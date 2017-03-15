
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
package com.trurating.trumodule;

import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.service.v210.xml.*;
import com.trurating.trumodule.util.TruModuleDateUtils;


@SuppressWarnings("Duplicates")
class XSD2TestFactory {

    private final ITruModuleProperties properties;

    XSD2TestFactory(ITruModuleProperties properties) {
        this.properties=properties;
    }



    RequestPosEvent generateRequestPosEvent() {
        RequestPosEvent event = new RequestPosEvent();
        RequestPosStartTilling startTilling = new RequestPosStartTilling();

        event.setStartTilling(startTilling);
        return event;
    }

    Response generateResponseForPosEvent() {
        Response response = new Response();
        response.setMerchantId("2");
        response.setPartnerId(properties.getPartnerId());
        response.setTerminalId("3");
        response.setSessionId(TruModuleDateUtils.timeNow().toString());
        return response;
    }

    Response generateResponseForQuestion() {
        Response response = new Response();
        response.setMerchantId("2");
        response.setPartnerId(properties.getPartnerId());
        response.setTerminalId("3");
        ResponseEvent responseEvent = new ResponseEvent();
        ResponseEventQuestion responseEventQuestion = new ResponseEventQuestion();
        responseEventQuestion.setTrigger(Trigger.DWELLTIME);
        responseEvent.setQuestion(responseEventQuestion);
        response.setEvent(responseEvent);

        response.setSessionId(TruModuleDateUtils.timeNow().toString());

        ResponseDisplay responseDisplay = new ResponseDisplay();
        ResponseLanguage responseLanguage  = new ResponseLanguage();
        responseLanguage.setRfc1766("en-GB");
        ResponseQuestion responseQuestion = new ResponseQuestion();
        responseQuestion.setMax((short)1);
        responseQuestion.setMin((short)20);
        responseQuestion.setTimeoutMs(5000);
        responseQuestion.setValue("Please customerBeginsTransactionAtTillPoint 0-9");

        ResponseScreen responseScreenNoRated = new ResponseScreen();
        responseScreenNoRated.setTimeoutMs(5000);
        responseScreenNoRated.setValue("Sorry you didn't customerBeginsTransactionAtTillPoint");
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
        responseReceiptNotRated.setValue("Sorry you didn't customerBeginsTransactionAtTillPoint");
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
        responseScreenNoRated2.setValue("Lo siento no customerBeginsTransactionAtTillPoint-screen");
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
        responseReceiptRated2.setValue("Lo siento no customerBeginsTransactionAtTillPoint-receipt");
        ResponseReceipt responseReceiptNotRated2 = new ResponseReceipt();
        responseReceiptNotRated2.setWhen(When.RATED);
        responseReceiptNotRated2.setValue("Gracias para rating-receipt");

        responseLanguage2.getReceipt().add(responseReceiptRated2);
        responseLanguage2.getReceipt().add(responseReceiptNotRated2);

        responseDisplay.getLanguage().add(responseLanguage2);
        response.setDisplay(responseDisplay);

        return response;
    }


    RequestTransaction assembleRequestTransaction() {

        RequestTransaction requestTransaction = new RequestTransaction();
        requestTransaction.setAmount(199);
        RequestTender requestTender = new RequestTender();
        requestTender.setAmount(199);
        requestTender.setCardHash(new RequestCardHash());
        requestTender.setEntryMode("ENTRY");
        requestTransaction.setResult(TransactionResult.APPROVED);
        requestTransaction.getTender().add(requestTender);

        return requestTransaction;
    }

    Request generateRequestToDeliverTransaction() {
        //comment
        Request request = new Request();
//        request.setMerchantId(properties.getMerchantId());
        request.setPartnerId(properties.getPartnerId());
//        request.setTerminalId(properties.getTerminalId());

        request.setSessionId(TruModuleDateUtils.timeNow().toString());

        RequestPeripheral requestPeripheral = new RequestPeripheral();
        if (requestPeripheral.getFont() == Font.MONOSPACED) requestPeripheral.setUnit(UnitDimension.LINE);
        else requestPeripheral.setUnit(UnitDimension.PIXEL);

        RequestDevice requestDevice = new RequestDevice();
        requestDevice.setScreen(requestPeripheral);

        RequestQuestion requestQuestion = new RequestQuestion();
        requestQuestion.setDevice(requestDevice);
        RequestLanguage requestLanguage = new RequestLanguage();
//        requestLanguage.setRfc1766(properties.getRfc1766());
        requestQuestion.getLanguage().add(requestLanguage);
        Trigger trigger = Trigger.DWELLTIME;
        requestQuestion.setTrigger(trigger);
        request.setQuestion(requestQuestion);
        return request;
    }
}
