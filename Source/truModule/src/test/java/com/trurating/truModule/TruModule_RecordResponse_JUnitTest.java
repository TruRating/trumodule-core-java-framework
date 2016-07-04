package com.trurating.truModule;

import static mockit.Deencapsulation.setField;

import com.trurating.CachedTruModuleRatingObject;
import com.trurating.network.xml.IXMLNetworkMessenger;
import com.trurating.network.xml.TruRatingMessageFactory;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.properties.UnitTestProperties;
import com.trurating.service.v200.xml.*;
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

/**
 * Created by Paul on 10/03/2016.
 */
@SuppressWarnings("Duplicates")
@RunWith(JMockit.class)
public class TruModule_RecordResponse_JUnitTest {

    @Tested
    TruModule truModule;
    @Injectable
    IXMLNetworkMessenger xmlNetworkMessenger;
    @Injectable
    IDevice iDevice;
    @Injectable
    Logger log;
    @Injectable
    ITruModuleProperties properties;
    @Injectable
    TruRatingMessageFactory truRatingMessageFactory;
    @Injectable
    CachedTruModuleRatingObject cachedTruModuleRatingObject;

    private XSD2TestFactory testFactory;

    @Before
    public void setUp() {
        properties = UnitTestProperties.getInstance(); // Set of test properties
        testFactory = new XSD2TestFactory(properties);
        truModule = new TruModule(properties);
        cachedTruModuleRatingObject = new CachedTruModuleRatingObject();

        setField(truModule, "xmlNetworkMessenger", xmlNetworkMessenger);
        setField(truModule, "iDevice", iDevice);
        setField(truModule, "log", log);
        setField(truModule, "truRatingMessageFactory", truRatingMessageFactory);
        setField(truModule, "cachedTruModuleRatingObject", cachedTruModuleRatingObject);
    }

    @Test
    public void deliverySucceedsTest() {
        new Expectations() {{
            xmlNetworkMessenger.getResponseRatingFromRatingsDeliveryToService((Request) any);
            returns(testFactory.generateResponseForQuestion());
            times = 1;
            truRatingMessageFactory.assembleRatingsDeliveryRequest((ITruModuleProperties) any, (String)any);
            returns (testFactory.generateRequestForQuestion());
            times = 1;
        }};

        //set up a fake Rfc
        Response response = new Response();
        ResponseDisplay responseDisplay = new ResponseDisplay();
        responseDisplay.getLanguage().add(new ResponseLanguage());
        responseDisplay.getLanguage().get(0).setRfc1766("en-GB");
        response.setDisplay(responseDisplay);
        cachedTruModuleRatingObject.response = response;
        setField(truModule, "cachedTruModuleRatingObject", cachedTruModuleRatingObject);

        RequestTransaction transaction = new RequestTransaction();
        truModule.getCachedTruModuleRatingObject().transaction=transaction;
        int truModuleOutcome =truModule.deliverRating();
        Assert.assertTrue(truModuleOutcome==TruModule.RATING_DELIVERY_OUTCOME_SUCCEEDED);
    }

    @Test
    public void recordResponseDeliveryFailsTest() {
        new Expectations() {{
            truRatingMessageFactory.assembleRatingsDeliveryRequest((ITruModuleProperties) any, (String)any);
            returns (testFactory.generateRequestForQuestion());
            times = 1;
            xmlNetworkMessenger.getResponseRatingFromRatingsDeliveryToService((Request) any);
            returns(null);
            times = 0;
        }};

        RequestTransaction transaction = new RequestTransaction();
        truModule.getCachedTruModuleRatingObject().transaction=transaction;
        int truModuleOutcome =truModule.deliverRating();
        Assert.assertTrue(truModuleOutcome==TruModule.RATING_DELIVERY_OUTCOME_FAILED);
    }

    @Test
    public void testCancellationTruModule() {
        CachedTruModuleRatingObject cachedTruModuleRatingObject = new CachedTruModuleRatingObject();
        //fake out the that we are mid-question display
        cachedTruModuleRatingObject.cancelled=false; //at this point we are not cancelled

        setField(truModule, "cachedTruModuleRatingObject", cachedTruModuleRatingObject);

        truModule.cancelRating(); //now we should be cancelled

        cachedTruModuleRatingObject = truModule.getCachedTruModuleRatingObject();
        Assert.assertTrue(cachedTruModuleRatingObject.cancelled);
    }

    @Test
    public void requestQuestionFromServerAndCacheResultTest() {

        final Response response = testFactory.generateResponseForQuestion();

        new Expectations() {{
            xmlNetworkMessenger.getResponseQuestionFromService((Request) any);
            returns (response);
            times = 1;
        }};

        CachedTruModuleRatingObject cachedTruModuleRatingObject = truModule.requestQuestionFromServerAndCacheResult(testFactory.generateRequestForQuestion());

        Assert.assertEquals("Sorry you didn't rate", cachedTruModuleRatingObject.responseNoRating);
        Assert.assertEquals("Thanks for rating", cachedTruModuleRatingObject.receiptWithRating);
        Assert.assertEquals("Sorry you didn't rate", cachedTruModuleRatingObject.responseNoRating);
        Assert.assertEquals("Thanks for rating", cachedTruModuleRatingObject.responseWithRating);
    }

    @Test
    public void requestQuestionFromServerAndCacheResultSpanishLanguageTest() {

        final Response response = testFactory.generateResponseForQuestion();

        new Expectations() {{
            xmlNetworkMessenger.getResponseQuestionFromService((Request) any);
            returns (response);
            times = 1;
        }};

        truModule.setCurrentTransactionLanguageCode("es-mx");
        CachedTruModuleRatingObject cachedTruModuleRatingObject = truModule.requestQuestionFromServerAndCacheResult(testFactory.generateRequestForQuestion());

        Assert.assertEquals("Lo siento no rate!", cachedTruModuleRatingObject.responseNoRating);
        Assert.assertEquals("Gracias para rating", cachedTruModuleRatingObject.receiptWithRating);
        Assert.assertEquals("Lo siento no rate!", cachedTruModuleRatingObject.responseNoRating);
        Assert.assertEquals("Gracias para rating", cachedTruModuleRatingObject.responseWithRating);
    }
}


