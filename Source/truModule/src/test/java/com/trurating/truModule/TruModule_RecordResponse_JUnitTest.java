package com.trurating.truModule;

import static mockit.Deencapsulation.setField;

import com.trurating.CachedTruModuleObject;
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
    CachedTruModuleObject cachedTruModuleObject;

    private XSD2TestFactory testFactory;

    @Before
    public void setUp() {
        properties = UnitTestProperties.getInstance(); // Set of test properties
        testFactory = new XSD2TestFactory(properties);
        truModule = new TruModule(properties);
        cachedTruModuleObject = new CachedTruModuleObject();

        setField(truModule, "xmlNetworkMessenger", xmlNetworkMessenger);
        setField(truModule, "iDevice", iDevice);
        setField(truModule, "log", log);
        setField(truModule, "truRatingMessageFactory", truRatingMessageFactory);
        setField(truModule, "cachedTruModuleObject", cachedTruModuleObject);
    }

    /*
    This test will mock the behaviour of a succesful ratings delivery
    The expected behaviour when conditions are correct it that the delivery
    should return RATING_DELIVERY_OUTCOME_SUCCEEDED
     */
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
        cachedTruModuleObject.response = response;
        setField(truModule, "cachedTruModuleObject", cachedTruModuleObject);

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
            times = 1;
        }};

        RequestTransaction transaction = new RequestTransaction();
        truModule.getCachedTruModuleRatingObject().transaction=transaction;
        int truModuleOutcome =truModule.deliverRating();
        Assert.assertTrue(truModuleOutcome==TruModule.RATING_DELIVERY_OUTCOME_FAILED);
    }

    @Test
    public void testCancellationTruModule() {
        CachedTruModuleObject cachedTruModuleObject = new CachedTruModuleObject();
        //fake out the that we are mid-question display
        cachedTruModuleObject.cancelled=false; //at this point we are not cancelled

        setField(truModule, "cachedTruModuleObject", cachedTruModuleObject);

        truModule.cancelRating(); //now we should be cancelled

        cachedTruModuleObject = truModule.getCachedTruModuleRatingObject();
        Assert.assertTrue(cachedTruModuleObject.cancelled);
    }

    /*
    This test should cache the correct screen and receipt responses to an incoming question based
    off the setCurrentTransactionLanguageCode
     */
    @Test
    public void requestQuestionFromServerAndCacheResultTest() {

        final Response response = testFactory.generateResponseForQuestion();

        new Expectations() {{
            xmlNetworkMessenger.getResponseQuestionFromService((Request) any);
            returns (response);
            times = 1;
        }};

        CachedTruModuleObject cachedTruModuleObject = truModule.requestQuestionFromServerAndCacheResult(testFactory.generateRequestForQuestion());

        Assert.assertEquals("Sorry you didn't rate", cachedTruModuleObject.responseNoRating);
        Assert.assertEquals("Thanks for rating", cachedTruModuleObject.receiptWithRating);
        Assert.assertEquals("Sorry you didn't rate", cachedTruModuleObject.responseNoRating);
        Assert.assertEquals("Thanks for rating", cachedTruModuleObject.responseWithRating);
    }


    /*
    This test should cache the correct screen and receipt responses to an incoming question based
    off the setCurrentTransactionLanguageCode
     */
    @Test
    public void requestQuestionFromServerAndCacheResultSpanishLanguageTest() {

        final Response response = testFactory.generateResponseForQuestion();

        new Expectations() {{
            xmlNetworkMessenger.getResponseQuestionFromService((Request) any);
            returns (response);
            times = 1;
        }};

        truModule.setCurrentTransactionLanguageCode("es-mx");
        CachedTruModuleObject cachedTruModuleObject = truModule.requestQuestionFromServerAndCacheResult(testFactory.generateRequestForQuestion());

        Assert.assertEquals("Lo siento no rate!", cachedTruModuleObject.responseNoRating);
        Assert.assertEquals("Gracias para rating", cachedTruModuleObject.receiptWithRating);
        Assert.assertEquals("Lo siento no rate!", cachedTruModuleObject.responseNoRating);
        Assert.assertEquals("Gracias para rating", cachedTruModuleObject.responseWithRating);
    }
}


