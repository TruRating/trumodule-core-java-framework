package com.trurating.truModule;

import static mockit.Deencapsulation.setField;

import java.math.BigInteger;

import com.trurating.CachedTruModuleRatingObject;
import com.trurating.network.xml.IXMLNetworkMessenger;
import com.trurating.network.xml.TruRatingMessageFactory;
import com.trurating.properties.UnitTestProperties;
import com.trurating.service.v200.xml.Request;
import com.trurating.service.v200.xml.RequestRating;
import com.trurating.service.v200.xml.Response;
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
import com.trurating.properties.ITruModuleProperties;

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
            xmlNetworkMessenger.getResponseRatingDeliveryFromService((Request) any);
            returns(testFactory.generateResponseForQuestion());
            times = 1;
            truRatingMessageFactory.assembleRatingsDeliveryRequest((ITruModuleProperties)any, (String)any);
            returns (testFactory.generateRequestForQuestion());
            times = 1;
        }};

        Assert.assertTrue(truModule.deliverRating());
    }

    @Test
    public void recordResponseDeliveryFailsTest() {
        new Expectations() {{
            xmlNetworkMessenger.getResponseRatingDeliveryFromService((Request) any);
            returns(null);
            times = 1;
            truRatingMessageFactory.assembleRatingsDeliveryRequest((ITruModuleProperties)any, (String)any);
            returns (testFactory.generateRequestForQuestion());
            times = 0;
        }};

        boolean truModuleOutcome =truModule.deliverRating();
        Assert.assertFalse(truModuleOutcome);
    }
}


