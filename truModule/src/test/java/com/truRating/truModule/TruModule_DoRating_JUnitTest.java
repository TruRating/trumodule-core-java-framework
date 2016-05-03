package com.trurating.truModule;

import static mockit.Deencapsulation.setField;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.trurating.TruModule;
import com.trurating.device.IDevice;
import com.trurating.network.xml.IXMLNetworkMessenger;
import com.trurating.network.xml.TruRatingMessageFactory;
import com.trurating.prize.PrizeManager;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.properties.TruModuleProperties;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages;
import com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language;
import com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.DisplayElements.Question;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB.Rating;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Paul on 10/03/2016.
 */
@RunWith(JMockit.class)
public class TruModule_DoRating_JUnitTest {

    @Tested
    TruModule truModule;
    @Injectable
    IDevice iDevice;    
    @Injectable
    IXMLNetworkMessenger xmlNetworkMessenger;
    @Injectable
    PrizeManager checkForPrize;
    @Injectable
    Logger log;
    @Injectable
    ITruModuleProperties properties;
    @Injectable
    TruRatingMessageFactory truRatingMessageFactory;

    @Before
    public void setUp() {
        truModule =  new TruModule();
        setField(truModule, "xmlNetworkMessenger", xmlNetworkMessenger);
        setField(truModule, "checkForPrize", checkForPrize);
        setField(truModule, "iDevice", iDevice);
        setField(truModule, "log", log);
        setField(truModule, "truRatingMessageFactory", truRatingMessageFactory);
    }

    @Test
    public void doRatingTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, anyLong);
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns ("8");
            times = 1;
            checkForPrize.checkForAPrize((IDevice) any, (QuestionResponseJAXB)any, (String) any);
            returns ("555");
            times = 1;
            truRatingMessageFactory.createRatingRecord((ITruModuleProperties)any);
            returns (getRatingDeliveryJAXB());
            times = 1;
        }};

        truModule.doRating(properties);
        RatingDeliveryJAXB iRatingRecord = truModule.getRatingRecord(properties) ;
        Rating rating = iRatingRecord.getRating() ;

        Assert.assertNotNull(iRatingRecord.getTransaction().getDatetime());
        Assert.assertEquals(8, rating.getValue());
        Assert.assertEquals(12345, rating.getQid());
        Assert.assertEquals("555", rating.getPrizecode());
    }

    @Test
    public void doRatingServiceFailsTest() {

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, anyLong);
            returns(null);
            truRatingMessageFactory.createRatingRecord((ITruModuleProperties)any);
            returns (getRatingDeliveryJAXB());
            times = 1;
        }};

        TruModuleProperties properties = new TruModuleProperties() ;
        truModule.doRating(properties);
        RatingDeliveryJAXB iRatingRecord = truModule.getRatingRecord(properties) ;
        Assert.assertEquals(iRatingRecord.getRating().getValue(), TruModule.NO_RATING_VALUE);
        // We should have a value
    }


    @Test
    public void doRatingNoPrizeTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, anyLong);
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns ("8");
            times = 1;
            truRatingMessageFactory.createRatingRecord((ITruModuleProperties)any);
            returns (getRatingDeliveryJAXB());
            times = 1;
            checkForPrize.checkForAPrize((IDevice) any, (QuestionResponseJAXB)any, (String) any);
            returns ("");
            times = 1;
        }};

        truModule.doRating(properties);
        Rating rating = truModule.getRatingRecord(properties).getRating() ;
        Assert.assertEquals("", rating.getPrizecode());
    }

    @Test
    public void doRatingUserPressesCancelOnPedTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, anyLong);
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns ("-1");
            times = 1;
            truRatingMessageFactory.createRatingRecord((ITruModuleProperties)any);
            returns (getRatingDeliveryJAXB());
            times = 1;
            checkForPrize.checkForAPrize((IDevice) any, (QuestionResponseJAXB)any, (String) any);
            returns ("");
            times = 0;
        }};

        truModule.doRating(properties);
        Rating rating = truModule.getRatingRecord(properties).getRating() ;
        Assert.assertEquals(rating.getValue(), TruModule.USER_CANCELLED);
        Assert.assertEquals("", rating.getPrizecode());
    }

    @Test
    public void doPrizeCheckFailsTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, anyLong);
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns ("-1");
            times = 1;
            checkForPrize.checkForAPrize((IDevice) any, (QuestionResponseJAXB)any, (String) any);
            returns ("555");
            times = 0;
            truRatingMessageFactory.createRatingRecord((ITruModuleProperties)any);
            returns (getRatingDeliveryJAXB());
            times = 1;
        }};

        truModule.doRating(properties);
        Rating rating = truModule.getRatingRecord(properties).getRating() ;
        Assert.assertEquals(rating.getValue(), TruModule.USER_CANCELLED);
        Assert.assertEquals("", rating.getPrizecode());
    }
    
    private QuestionResponseJAXB getQuestionResponseJAXB() {
        QuestionResponseJAXB questionResponseJAXB = new QuestionResponseJAXB();
        final Question question = new Question();
        question.setValue("Rate 0-9");
        questionResponseJAXB.setMessagetype("MY MESSAGE TYPE");
        question.setQid(12345L);
        Languages languages = new Languages();
        Language language = new Language();
        language.setLanguagetype("EN-GB");

        Language.DisplayElements displayElements = new Language.DisplayElements();
        displayElements.setQuestion(question);
        language.setDisplayElements(displayElements);

        com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.Receipt receipt =
                new Language.Receipt();
        receipt.setRatedvalue("8");
        language.setReceipt(receipt);
<<<<<<< HEAD
        languages.addLanguage(language);
=======
        languages.getLanguage().add(language);
>>>>>>> origin/master

        questionResponseJAXB.setLanguages(languages);

        return questionResponseJAXB;
    }

    private RatingDeliveryJAXB getRatingDeliveryJAXB() {

        RatingDeliveryJAXB ratingDeliveryJAXB = new RatingDeliveryJAXB();
        ratingDeliveryJAXB.setErrortext("");
        RatingDeliveryJAXB.CardHash cardHash = new RatingDeliveryJAXB.CardHash();
        cardHash.setCardhashdata("cardHarhValue");
        cardHash.setCardhashdatatype("MD5");
        ratingDeliveryJAXB.setCardHash(cardHash);
        RatingDeliveryJAXB.Languages languages = new RatingDeliveryJAXB.Languages();
        RatingDeliveryJAXB.Languages.Language language = new RatingDeliveryJAXB.Languages.Language();
        language.setIncludereceipt(false);
        language.setLanguagetype("en-GB");
        languages.getLanguage().add(language);
        ratingDeliveryJAXB.setLanguages(languages);

        ratingDeliveryJAXB.setMid("12345");
        ratingDeliveryJAXB.setTid("12345");
        ratingDeliveryJAXB.setMessagetype("TYPE");
        Rating rating = new Rating();
        rating.setValue(TruModule.NO_RATING_VALUE);
        rating.setPrizecode("");
        rating.setResponsetimemilliseconds(5L);
        ratingDeliveryJAXB.setRating(rating);
        ratingDeliveryJAXB.setUid(new BigInteger("123456789"));

        Date now = new Date();
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

        return ratingDeliveryJAXB;
    }
}


