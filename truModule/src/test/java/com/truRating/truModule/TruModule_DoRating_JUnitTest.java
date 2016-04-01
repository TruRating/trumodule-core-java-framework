package com.truRating.truModule;

import static mockit.Deencapsulation.setField;

import java.math.BigDecimal;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.truRating.payment.TruModulePaymentRequest; 

import com.truRating.truModule.device.IDevice;
import com.truRating.truModule.network.xml.XMLNetworkMessenger;
import com.truRating.truModule.payment.IPaymentRequest;
import com.truRating.truModule.prize.CheckForPrize;
import com.truRating.truModule.properties.ITruModuleProperties;
import com.truRating.truModule.rating.Rating;
import com.truRating.truModule.xml.questionResponse.QuestionResponseJAXB;
import com.truRating.truModule.xml.questionResponse.QuestionResponseJAXB.Languages;
import com.truRating.truModule.xml.questionResponse.QuestionResponseJAXB.Languages.Language;
import com.truRating.truModule.xml.questionResponse.QuestionResponseJAXB.Languages.Language.DisplayElements.Question;

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
    IPaymentRequest iPaymentRequest;
    @Injectable
    XMLNetworkMessenger xmlNetworkMessenger;
    @Injectable
    CheckForPrize checkForPrize;
    @Injectable
    Logger log;

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
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any);
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displaySecurePromptGetKeystroke((String[])any, (String)any, anyInt);
            returns ("8");
            times = 1;
            checkForPrize.doCheck((IDevice) any, (QuestionResponseJAXB)any, (String)any);
            returns ("555");
            times = 1;
        }};

        Rating iRating = truModule.doRating(new TruModulePaymentRequest("A fluffy teddy", 199), null);

        Assert.assertNotNull(iRating.getRatingTime());
        Assert.assertEquals(8, iRating.getValue());
        Assert.assertEquals(12345L, iRating.getQuestionID());
        Assert.assertEquals("555", iRating.getPrizeCode());
    }

    @Test
    public void doRatingServiceFailsTest() {

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any);
            returns(null);
            times = 1;
        }};

        Rating rating = truModule.doRating(new TruModulePaymentRequest("A fluffy teddy", 199), null);
        Assert.assertNull(rating);
    }


    @Test
    public void doRatingNoPrizeTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any);
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displaySecurePromptGetKeystroke((String[])any, (String)any, anyInt);
            returns ("8");
            times = 1;
            checkForPrize.doCheck((IDevice) any, (QuestionResponseJAXB)any, (String)any);
            returns ("");
            times = 1;
        }};

        Rating rating = truModule.doRating(new TruModulePaymentRequest("A fluffy teddy", 199), null);
        Assert.assertEquals("", rating.getPrizeCode());
    }

    @Test
    public void doRatingUserPressesCancelOnPedTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any);
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displaySecurePromptGetKeystroke((String[])any, (String)any, anyInt);
            returns ("-1");
            times = 1;
            checkForPrize.doCheck((IDevice) any, (QuestionResponseJAXB)any, (String)any);
            returns ("");
            times = 0;
        }};

        Rating rating = truModule.doRating(new TruModulePaymentRequest("A fluffy teddy", 199), null);

        Assert.assertEquals("", rating.getPrizeCode());
    }

    @Test
    public void doPrizeCheckFailsTest() {

        final QuestionResponseJAXB questionResponseJAXB = getQuestionResponseJAXB();

        new Expectations() {{
            xmlNetworkMessenger.getQuestionFromService((ITruModuleProperties)any);
            returns(questionResponseJAXB);
            times = 1;
            iDevice.displaySecurePromptGetKeystroke((String[])any, (String)any, anyInt);
            returns ("1");
            times = 1;
            checkForPrize.doCheck((IDevice) any, (QuestionResponseJAXB)any, (String)any);
            returns (null);
            times = 1;
        }};

        Rating rating = truModule.doRating(new TruModulePaymentRequest("A fluffy teddy", 199), null);

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


