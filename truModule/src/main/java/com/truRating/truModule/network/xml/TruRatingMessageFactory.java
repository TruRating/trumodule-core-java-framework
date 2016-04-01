package com.truRating.truModule.network.xml;

import com.truRating.truModule.payment.IPaymentResponse;
import com.truRating.truModule.properties.ITruModuleProperties;
import com.truRating.truModule.rating.Rating;
import com.truRating.truModule.xml.questionRequest.QuestionRequestJAXB;
import com.truRating.truModule.xml.ratingDelivery.RatingDeliveryJAXB;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Paul on 09/03/2016.
 */
public class TruRatingMessageFactory {

    Logger log = Logger.getLogger(TruRatingMessageFactory.class);

    public QuestionRequestJAXB assembleARequestQuestion(ITruModuleProperties properties) {
        QuestionRequestJAXB questionRequestJAXB = null;
        try {
            questionRequestJAXB = new QuestionRequestJAXB();
            QuestionRequestJAXB.Languages.Language language = new QuestionRequestJAXB.Languages.Language();
            language.setLanguagetype(properties.getLanguageCode());
            language.setIncludeacknowledgement(new Boolean(properties.getIncludeAcknowledgement()));
            language.setIncludereceipt(new Boolean(properties.getIncludeReceipt()));
            QuestionRequestJAXB.Languages languages = new QuestionRequestJAXB.Languages();
            languages.getLanguage().add(language);
            questionRequestJAXB.setLanguages(languages);

            QuestionRequestJAXB.DeviceInfo deviceInfoType = new QuestionRequestJAXB.DeviceInfo();
            deviceInfoType.setDevice(properties.getDeviceType());
            deviceInfoType.setFirmware(properties.getDeviceFirmware());
            Byte b = new Byte(properties.getDevicenLines());
            deviceInfoType.setNlines(b.byteValue());
            b = new Byte(properties.getDeviceCpl());
            deviceInfoType.setCpl(b.byteValue());
            deviceInfoType.setFormat(properties.getDeviceFormat());
            deviceInfoType.setFonttype(properties.getDeviceFontType());
            questionRequestJAXB.setDeviceInfo(deviceInfoType);

            QuestionRequestJAXB.ServerInfo serverInfo = new QuestionRequestJAXB.ServerInfo();
            serverInfo.setServerid(properties.getServerId());
            serverInfo.setPpafirmware(properties.getPpaFirmware());
            questionRequestJAXB.setServerInfo(serverInfo);

            questionRequestJAXB.setMessagetype("QuestionRequest");
            questionRequestJAXB.setTid(properties.getTid());
            questionRequestJAXB.setUid(new BigInteger(properties.getUid()));
            questionRequestJAXB.setMid(properties.getMid());

        } catch (NumberFormatException e) {
            log.error("There was an error assembling the questionRequestJAXB ", e);
        }

        return questionRequestJAXB;
    }

    public RatingDeliveryJAXB assembleARatingDelivery(Rating rating, IPaymentResponse paymentResponse, ITruModuleProperties properties) throws NumberFormatException {
        RatingDeliveryJAXB ratingDeliveryJAXB = null;
        ratingDeliveryJAXB = new RatingDeliveryJAXB();

        ratingDeliveryJAXB.setMessagetype("RatingDelivery");
        ratingDeliveryJAXB.setTid(properties.getTid());
        ratingDeliveryJAXB.setUid(new BigInteger(properties.getUid()));
        ratingDeliveryJAXB.setMid(properties.getMid());

        RatingDeliveryJAXB.Rating ratingElement = new RatingDeliveryJAXB.Rating();
        ratingElement.setValue((short) rating.getValue());
        ratingElement.setResponsetimemilliseconds(new Short(rating.getRatingTime()));
        ratingElement.setQid(rating.getQuestionID());
        ratingElement.setPrizecode(rating.getPrizeCode());
        ratingElement.setRatinglanguage(properties.getLanguageCode());
        ratingDeliveryJAXB.setRating(ratingElement);

        RatingDeliveryJAXB.Languages.Language language = new RatingDeliveryJAXB.Languages.Language();
        language.setLanguagetype(properties.getLanguageCode());
        language.setIncludereceipt(true);
        RatingDeliveryJAXB.Languages languages = new RatingDeliveryJAXB.Languages();
        languages.setLanguage(language);
        ratingDeliveryJAXB.setLanguages(languages);

        RatingDeliveryJAXB.Transaction transaction = new RatingDeliveryJAXB.Transaction();
        Long dateAsLong = new Date().getTime();
        transaction.setTxnid(dateAsLong.longValue());
        transaction.setDatetime(paymentResponse.getTransactionDate().toString() + " "
                + paymentResponse.getTransactionTime());
        transaction.setAmount(new Long(paymentResponse.getTransactionAmount()).intValue());
        transaction.setGratuity(0);
        transaction.setCurrency((short) 643);
        transaction.setCardtype(paymentResponse.getCardScheme());
        transaction.setEntrymode("021");
        transaction.setTendertype(paymentResponse.getTenderType().toString());

        if (paymentResponse.isApproved()) {
            transaction.setResult("Approved");
        } else {
            transaction.setResult("Declined");
        }

        transaction.setOperator(paymentResponse.getOperatorId());
        ratingDeliveryJAXB.setTransaction(transaction);

        RatingDeliveryJAXB.CardHash cardHash = new RatingDeliveryJAXB.CardHash();
        cardHash.setCardhashdatatype(paymentResponse.getCardHashType());
        cardHash.setCardhashdata(paymentResponse.getCardHashData());
        ratingDeliveryJAXB.setCardHash(cardHash);

        ratingDeliveryJAXB.setErrorcode(new BigInteger("0"));
        ratingDeliveryJAXB.setErrortext("No error");

        return ratingDeliveryJAXB;
    }

}
