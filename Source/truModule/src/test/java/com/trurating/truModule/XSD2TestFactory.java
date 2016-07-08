package com.trurating.truModule;

import com.trurating.properties.ITruModuleProperties;
import com.trurating.service.v200.xml.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Paul on 02/06/2016.
 */
public class XSD2TestFactory {

    private final ITruModuleProperties properties;

    public XSD2TestFactory(ITruModuleProperties properties) {
        this.properties=properties;
    }

    private Response generateResponseFromDelivery() {
        Response response = generateResponseForQuestion();
        List<ResponseLanguage> language = response.getDisplay().getLanguage();
        //todo
        return response;
    }
    public Response generateResponseForQuestion() {
        Response response = new Response();
        response.setMerchantId(properties.getMid());
        response.setPartnerId(properties.getPartnerId());
        response.setTerminalId(properties.getTid());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String session = sdf.format(new Date());
        response.setSessionId(session);

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
        responseScreenNoRated2.setValue("Lo siento no rate!");
        responseScreenNoRated2.setWhen(When.NOTRATED);
        responseLanguage2.getScreen().add(responseScreenNoRated2);

        ResponseScreen responseScreenRated2 = new ResponseScreen();
        responseScreenRated2.setTimeoutMs(5000);
        responseScreenRated2.setValue("Gracias para rating");
        responseScreenRated2.setWhen(When.RATED);
        responseLanguage2.getScreen().add(responseScreenRated2);

        responseLanguage2.setQuestion(responseQuestion2);

        ResponseReceipt responseReceiptRated2 = new ResponseReceipt();
        responseReceiptRated2.setWhen(When.NOTRATED);
        responseReceiptRated2.setValue("Lo siento no rate");
        ResponseReceipt responseReceiptNotRated2 = new ResponseReceipt();
        responseReceiptNotRated2.setWhen(When.RATED);
        responseReceiptNotRated2.setValue("Gracias para rating");

        responseLanguage2.getReceipt().add(responseReceiptRated2);
        responseLanguage2.getReceipt().add(responseReceiptNotRated2);

        responseDisplay.getLanguage().add(responseLanguage2);
        response.setDisplay(responseDisplay);

        return response;
    }


    public Request generateRequestForQuestion() {
        Request request = new Request();
        request.setMerchantId(properties.getMid());
        request.setPartnerId(properties.getPartnerId());
        request.setTerminalId(properties.getTid());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String session = sdf.format(new Date());
        request.setSessionId(session);

        RequestPeripheral requestPeripheral = new RequestPeripheral();
        requestPeripheral.setFont(Font.valueOf(properties.getDeviceFontType()));
        requestPeripheral.setFormat(Format.valueOf(properties.getDeviceFormat()));
        requestPeripheral.setHeight((short) properties.getDeviceLines());
        if (requestPeripheral.getFont() == Font.MONOSPACED) requestPeripheral.setUnit(Unit.LINE);
        else requestPeripheral.setUnit(Unit.PIXEL);

        requestPeripheral.setWidth((short) properties.getDeviceCPL());

        RequestDevice requestDevice = new RequestDevice();
        requestDevice.setFirmware(properties.getPpaFirmware());
        requestDevice.setName(properties.getDeviceType());
        requestDevice.setScreen(requestPeripheral);

        RequestQuestion requestQuestion = new RequestQuestion();
        requestQuestion.setDevice(requestDevice);
        RequestLanguage requestLanguage = new RequestLanguage();
        requestLanguage.setRfc1766(properties.getLanguageCode());
        requestQuestion.getLanguage().add(requestLanguage);
        Trigger trigger = Trigger.DWELLTIME;
        requestQuestion.setTrigger(trigger);
        request.setQuestion(requestQuestion);

        return request;
    }

    private Request generateRequestForDelivery() {
        Request request = new Request();
        request.setMerchantId(properties.getMid());
        request.setPartnerId(properties.getPartnerId());
        request.setTerminalId(properties.getTid());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sessionID = sdf.format(new Date());
        request.setSessionId(sessionID);

        RequestRating requestRating = new RequestRating();
        requestRating.setRfc1766(properties.getLanguageCode());
        requestRating.setDateTime(Long.toString(new Date().getTime()));
        return request;
    }
}
