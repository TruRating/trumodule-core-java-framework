package com.truRating.truModule;

import com.truRating.truModule.network.xml.XMLNetworkMessenger;
import com.truRating.truModule.payment.TruModulePaymentResponse;
import com.truRating.truModule.properties.ITruModuleProperties;
import com.truRating.truModule.rating.Rating;
import com.truRating.truSharedData.payment.IPaymentApplication;
import com.truRating.truModule.xml.ratingResponse.RatingResponseJAXB;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;

import static com.truRating.truModule.xml.ratingResponse.RatingResponseJAXB.Languages;
import static com.truRating.truModule.xml.ratingResponse.RatingResponseJAXB.Languages.Language;
import static com.truRating.truModule.xml.ratingResponse.RatingResponseJAXB.Languages.Language.Receipt;
import static mockit.Deencapsulation.setField;

/**
 * Created by Paul on 10/03/2016.
 */
@RunWith(JMockit.class)
public class TruModule_RecordResponse_JUnitTest {

    @Tested
    TruModule truModule;
    @Injectable
    XMLNetworkMessenger xmlNetworkMessenger;
    @Injectable
    IPaymentApplication paymentApplication;
    @Injectable
    Logger log;
    @Injectable
    ITruModuleProperties properties;

    @Before
    public void setUp() {
        truModule = new TruModule();
        setField(truModule, "xmlNetworkMessenger", xmlNetworkMessenger);
        setField(truModule, "paymentApplication", paymentApplication);
        setField(truModule, "log", log);

    }

    @Test
    public void recordResponseTest() {

        final RatingResponseJAXB ratingResponseJAXB= getRatingResponseMockJAXBTest();

        new Expectations() {{
            xmlNetworkMessenger.deliveryRatingToService((Rating) any, (TruModulePaymentResponse) any, (ITruModuleProperties) any);
            returns(ratingResponseJAXB);
            times = 1;
        }};

        Rating rating = new Rating();
        rating.setValue(8);

        boolean methodSucceeded = truModule.recordRatingResponse(new TruModulePaymentResponse(), rating, properties);
        Assert.assertEquals(true, methodSucceeded);
    }

    @Test
    public void recordResponseDeliveryFailsTest() {

        new Expectations() {{
            xmlNetworkMessenger.deliveryRatingToService((Rating) any, (TruModulePaymentResponse) any, (ITruModuleProperties) any);
            returns(null);
            times = 1;
        }};

        Rating rating = new Rating();

        boolean methodSucceeded = truModule.recordRatingResponse(new TruModulePaymentResponse(), rating, properties);
        Assert.assertEquals(false, methodSucceeded);
    }

    @Test
    public void paymentApplicationDeviceNotAvailableTest() {

        new Expectations() {{
            paymentApplication.getDevice().displayMessage(anyString);
            result = new NullPointerException();
            times = 1;
        }};

        Rating rating = new Rating();
        rating.setValue(8);

        boolean methodSucceeded = truModule.recordRatingResponse(new TruModulePaymentResponse(), rating, properties);
        Assert.assertEquals(false, methodSucceeded);
    }

    private RatingResponseJAXB getRatingResponseMockJAXBTest() {
        RatingResponseJAXB ratingResponseJAXB = new RatingResponseJAXB();
        Languages languages = new Languages();
        Language language = new Language();

        Receipt receipt = new Receipt();
        receipt.setRatedvalue("7");
        receipt.setNotratedvalue("8");
        language.setReceipt(receipt);
        language.setIncludereceipt(true);
        language.setLanguagetype("EN-GB");
        languages.setLanguage(language);
        ratingResponseJAXB.setLanguages(languages);

        ratingResponseJAXB.setErrorcode(new BigInteger("199"));
        ratingResponseJAXB.setErrortext("Some error text");
        ratingResponseJAXB.setMessagetype("A message type");
        ratingResponseJAXB.setMid("MID1");
        ratingResponseJAXB.setTid("TID1");
        ratingResponseJAXB.setUid(new BigInteger("15"));
        return ratingResponseJAXB;
    }
}


