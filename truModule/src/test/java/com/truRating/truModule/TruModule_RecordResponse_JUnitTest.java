package com.trurating.truModule;

import static mockit.Deencapsulation.setField;

import java.math.BigInteger;

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
import com.trurating.network.xml.XMLNetworkMessenger;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB.Rating;
import com.trurating.xml.ratingResponse.RatingResponseJAXB;
import com.trurating.xml.ratingResponse.RatingResponseJAXB.Languages;
import com.trurating.xml.ratingResponse.RatingResponseJAXB.Languages.Language;
import com.trurating.xml.ratingResponse.RatingResponseJAXB.Languages.Language.Receipt;

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
    IDevice iDevice;
    @Injectable
    Logger log;
    @Injectable
    ITruModuleProperties properties;

    @Before
    public void setUp() {
        truModule = new TruModule();
        setField(truModule, "xmlNetworkMessenger", xmlNetworkMessenger);
        setField(truModule, "iDevice", iDevice);
        setField(truModule, "log", log);

    }

    @Test
    public void recordResponseTest() {

        final RatingResponseJAXB ratingResponseJAXB= getRatingResponseMockJAXBTest();

        new Expectations() {{
            xmlNetworkMessenger.deliverRatingToService((RatingDeliveryJAXB) any);
            returns(ratingResponseJAXB);
            times = 1;
        }};

        RatingDeliveryJAXB iRatingRecord = truModule.getRatingRecord(properties) ;
        Rating rating = iRatingRecord.getRating() ;
        rating.setValue((short)8);

        boolean methodSucceeded = truModule.deliverRating(properties);
        Assert.assertEquals(true, methodSucceeded);
    }

    @Test
    public void recordResponseDeliveryFailsTest() {

        new Expectations() {{
            xmlNetworkMessenger.deliverRatingToService((RatingDeliveryJAXB) any);
            returns(null);
            times = 0;
        }};

        boolean methodSucceeded = truModule.deliverRating(properties);
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

        ratingResponseJAXB.setErrorcode(new BigInteger("0"));
        ratingResponseJAXB.setErrortext("");
        ratingResponseJAXB.setMessagetype("A message type");
        ratingResponseJAXB.setMid("MID1");
        ratingResponseJAXB.setTid("TID1");
        ratingResponseJAXB.setUid(new BigInteger("15"));
        return ratingResponseJAXB;
    }
}


