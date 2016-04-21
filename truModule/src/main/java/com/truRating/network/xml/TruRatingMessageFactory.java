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

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.trurating.TruModule;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.properties.TruModuleProperties;
import com.trurating.xml.questionRequest.QuestionRequestJAXB;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB.Rating;

/**
 * Created by Paul on 09/03/2016.
 */
public class TruRatingMessageFactory {

    Logger log = Logger.getLogger(TruRatingMessageFactory.class);

    public QuestionRequestJAXB assembleARequestQuestion(ITruModuleProperties propertiesProvided, long transactionId) {
        QuestionRequestJAXB questionRequestJAXB = null;
		ITruModuleProperties properties = checkProperties(propertiesProvided) ;

		try {
            questionRequestJAXB = new QuestionRequestJAXB();

            questionRequestJAXB.setErrortext("");
            questionRequestJAXB.setErrorcode(new BigInteger("0"));

            QuestionRequestJAXB.Languages.Language language = new QuestionRequestJAXB.Languages.Language();
            language.setLanguagetype(properties.getLanguageCode());
            language.setIncludeacknowledgement(new Boolean(properties.getIncludeAcknowledgement()));
            language.setIncludereceipt(new Boolean(properties.getIncludeReceipt()));
            QuestionRequestJAXB.Languages languages = new QuestionRequestJAXB.Languages();
            languages.addLanguage(language);
            questionRequestJAXB.setLanguages(languages);

            QuestionRequestJAXB.DeviceInfo deviceInfoType = new QuestionRequestJAXB.DeviceInfo();
            deviceInfoType.setDevice(properties.getDeviceType());
            deviceInfoType.setFirmware(properties.getDeviceFirmware());
            deviceInfoType.setNlines((byte) properties.getDeviceLines());
            deviceInfoType.setCpl((byte) properties.getDeviceCpl());
            deviceInfoType.setFormat(properties.getDeviceFormat());
            deviceInfoType.setFonttype(properties.getDeviceFontType());
            deviceInfoType.setReceiptwidth((byte)20);
            questionRequestJAXB.setDeviceInfo(deviceInfoType);

            QuestionRequestJAXB.ServerInfo serverInfo = new QuestionRequestJAXB.ServerInfo();
            serverInfo.setServerid(properties.getServerId());
            serverInfo.setPpafirmware(properties.getPpaFirmware());
            questionRequestJAXB.setServerInfo(serverInfo);

            questionRequestJAXB.setMessagetype("QuestionRequest");
            questionRequestJAXB.setTid(properties.getTid());
            questionRequestJAXB.setUid(new BigInteger(String.valueOf(transactionId)));
            questionRequestJAXB.setMid(properties.getMid());

        } catch (NumberFormatException e) {
            log.error("There was an error assembling the questionRequestJAXB ", e);
        }

        return questionRequestJAXB;
    }

    public RatingDeliveryJAXB createRatingRecord(ITruModuleProperties propertiesProvided) {
        RatingDeliveryJAXB ratingDeliveryJAXB = new RatingDeliveryJAXB();
        Rating rating = ratingDeliveryJAXB.getRating() ; 
		Date now = new Date();
		
		ITruModuleProperties properties = checkProperties(propertiesProvided) ;

        ratingDeliveryJAXB.setMessagetype("RatingDelivery");
        ratingDeliveryJAXB.setTid(properties.getTid());
        ratingDeliveryJAXB.setMid(properties.getMid());
        ratingDeliveryJAXB.setUid(new BigInteger(String.valueOf(now.getTime())));

        // Rating element
        RatingDeliveryJAXB.Rating ratingElement = new RatingDeliveryJAXB.Rating();
        ratingElement.setValue(TruModule.NO_RATING_VALUE); // No rating value
        ratingElement.setResponsetimemilliseconds(0L);
        ratingElement.setQid(0L);
        ratingElement.setPrizecode("");
        ratingElement.setRatinglanguage(properties.getLanguageCode());
        ratingDeliveryJAXB.setRating(ratingElement);

        // Language element
        RatingDeliveryJAXB.Languages.Language language = new RatingDeliveryJAXB.Languages.Language();
        language.setLanguagetype(properties.getLanguageCode());
        language.setIncludereceipt(true);
        RatingDeliveryJAXB.Languages languages = new RatingDeliveryJAXB.Languages();
        languages.addLanguage(language);
        ratingDeliveryJAXB.setLanguages(languages);

        // Transaction element
        RatingDeliveryJAXB.Transaction transaction = new RatingDeliveryJAXB.Transaction();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		transaction.setDatetime(sdf.format(now));
        transaction.setTxnid(now.getTime());
        transaction.setAmount(0);
        transaction.setGratuity(0);
        transaction.setCurrency((short) 826);
        transaction.setCardtype("");
        transaction.setEntrymode("021");
        transaction.setTendertype("");
        transaction.setResult("");
        transaction.setOperator("");
        ratingDeliveryJAXB.setTransaction(transaction);

        RatingDeliveryJAXB.CardHash cardHash = new RatingDeliveryJAXB.CardHash();
        cardHash.setCardhashdatatype("");
        cardHash.setCardhashdata("");
        ratingDeliveryJAXB.setCardHash(cardHash);

        ratingDeliveryJAXB.setErrorcode(new BigInteger("0"));
        ratingDeliveryJAXB.setErrortext("");

        return ratingDeliveryJAXB;
    }


    private ITruModuleProperties checkProperties (ITruModuleProperties properties) {
    	if (properties != null)
    		return properties ;
    	log.error("Null properties given to TruRatingMessageFactory");
    	return new TruModuleProperties() ;
    }
}
