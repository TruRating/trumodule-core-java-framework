/*
 * @(#)TruRatingMessageFactory.java
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

package com.trurating.network.xml;

import java.util.Date;

import com.trurating.service.v200.xml.*;
import org.apache.log4j.Logger;

import com.trurating.properties.ITruModuleProperties;
import com.trurating.properties.TruModuleProperties;

/**
 * Created by Paul on 09/03/2016.
 */
public class TruRatingMessageFactory {

    Logger log = Logger.getLogger(TruRatingMessageFactory.class);

    public Request assembleQuestionRequest(ITruModuleProperties propertiesProvided, String sessionID) {

        ITruModuleProperties properties = checkProperties(propertiesProvided);

        Request request = new Request();
        request.setMerchantId(properties.getMid());
        request.setPartnerId(properties.getPartnerId());
        request.setTerminalId(properties.getTid());
        request.setSessionId(sessionID);

        try {

            ResponseLanguage language = new ResponseLanguage();
            language.setRfc1766(properties.getLanguageCode());

            RequestPeripheral requestPeripheral = new RequestPeripheral();
            requestPeripheral.setFont(Font.valueOf(properties.getDeviceFontType()));
            requestPeripheral.setFormat(Format.valueOf(properties.getDeviceFormat()));
            requestPeripheral.setHeight((short) properties.getDeviceLines());
            if (requestPeripheral.getFont() == Font.MONOSPACED) requestPeripheral.setUnit(Unit.LINE);
            else requestPeripheral.setUnit(Unit.PIXEL);

            requestPeripheral.setWidth((short) properties.getDeviceCpl());

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

        } catch (NumberFormatException e) {
            log.error("There was an error assembling the request for a question ", e);
            return null;
        }

        return request;
    }
    public Request assembleRatingsDeliveryRequest(ITruModuleProperties propertiesProvided, String sessionID) {

        ITruModuleProperties properties = checkProperties(propertiesProvided);

        Request request = new Request();
        request.setMerchantId(properties.getMid());
        request.setPartnerId(properties.getPartnerId());
        request.setTerminalId(properties.getTid());
        request.setSessionId(sessionID);

        RequestRating requestRating = new RequestRating();
        requestRating.setRfc1766(properties.getLanguageCode());
        requestRating.setDateTime(Long.toString(new Date().getTime()));

        return request;
    }

    private ITruModuleProperties checkProperties(ITruModuleProperties properties) {
        if (properties != null)
            return properties;
        log.error("Null properties given to TruRatingMessageFactory");
        return new TruModuleProperties();
    }
}
