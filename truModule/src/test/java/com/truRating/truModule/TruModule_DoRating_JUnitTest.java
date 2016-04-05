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
import com.trurating.payment.TruModulePaymentRequest;
import com.trurating.prize.PrizeManager;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.rating.Rating;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages;
import com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language;
import com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.DisplayElements.Question;

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

    @Before
    public void setUp() {
        truModule =  new TruModule();
        setField(truModule, "xmlNetworkMessenger", xmlNetworkMessenger);
        setField(truModule, "checkForPrize", checkForPrize);
        setField(truModule, "iDevice", iDevice);
        setField(truModule, "log", log);
    }

    @Test
    public void myTestTest() {
        Assert.assertEquals(1,1);
    }

    @Test
    public void doRatingTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, "12345");
            returns(questionResponseJAXB);
            times = 1;
//            iDevice.displaySecurePromptGetKeystroke((String[])any, (String)any, anyInt);
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns ("8");
            times = 1;
            checkForPrize.checkForAPrize((IDevice) any, (QuestionResponseJAXB)any, (String)any);
            returns ("555");
            times = 1;
        }};

        truModule.doRating(properties, new TruModulePaymentRequest("A fluffy teddy", 199));
        Rating iRating = truModule.getTransactionContext().getRating() ;

        Assert.assertNotNull(iRating.getRatingDateTime());
        Assert.assertEquals(8, iRating.getValue());
        Assert.assertEquals(12345L, iRating.getQuestionID());
        Assert.assertEquals("555", iRating.getPrizeCode());
    }

    @Test
    public void doRatingServiceFailsTest() {

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, "12345");
            returns(null);
            times = 1;
        }};

        truModule.doRating(null, new TruModulePaymentRequest("A fluffy teddy", 199));
        Rating rating = truModule.getTransactionContext().getRating() ;
        Assert.assertNull(rating);
    }


    @Test
    public void doRatingNoPrizeTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, "12345");
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns ("8");
            times = 1;
            checkForPrize.checkForAPrize((IDevice) any, (QuestionResponseJAXB)any, (String)any);
            returns ("");
            times = 1;
        }};

        truModule.doRating(null, new TruModulePaymentRequest("A fluffy teddy", 199));
        Rating rating = truModule.getTransactionContext().getRating() ;
        Assert.assertEquals("", rating.getPrizeCode());
    }

    @Test
    public void doRatingUserPressesCancelOnPedTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, "12345");
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns ("-1");
            times = 1;
            checkForPrize.checkForAPrize((IDevice) any, (QuestionResponseJAXB)any, (String)any);
            returns ("");
            times = 0;
        }};

        truModule.doRating(null, new TruModulePaymentRequest("A fluffy teddy", 199));
        Rating rating = truModule.getTransactionContext().getRating() ;

        Assert.assertEquals("", rating.getPrizeCode());
    }

    @Test
    public void doPrizeCheckFailsTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any, "12345");
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns ("1");
            times = 1;
            checkForPrize.checkForAPrize((IDevice) any, (QuestionResponseJAXB)any, (String)any);
            returns (null);
            times = 1;
        }};

        truModule.doRating(null, new TruModulePaymentRequest("A fluffy teddy", 199));
        Rating rating = truModule.getTransactionContext().getRating() ;

        Assert.assertEquals("", rating.getPrizeCode());
    }
    private QuestionResponseJAXB getQuestionResponseJAXB() {
        QuestionResponseJAXB questionResponseJAXB = new QuestionResponseJAXB();
        final Question question = new Question();
        question.setValue("Rate 0-9");
        question.setQid(12345L);
        Languages languages = new Languages();
        Language language = new Language();
        languages.setLanguage(language);
        Language.DisplayElements displayElements = new Language.DisplayElements();
        language.setDisplayElements(displayElements);

        questionResponseJAXB.setLanguages(languages);

        questionResponseJAXB.getLanguages().getLanguage().getDisplayElements().setQuestion(question);
        return questionResponseJAXB;
    }
}


