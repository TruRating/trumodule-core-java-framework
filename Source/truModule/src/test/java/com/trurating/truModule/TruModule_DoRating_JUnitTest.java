package com.trurating.truModule;

import static mockit.Deencapsulation.setField;

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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.trurating.TruModule;
import com.trurating.device.IDevice;
import com.trurating.network.xml.IXMLNetworkMessenger;
import com.trurating.network.xml.TruRatingMessageFactory;

/**
 * Created by Paul on 10/03/2016.
 */
@SuppressWarnings("Duplicates")
@RunWith(JMockit.class)
public class TruModule_DoRating_JUnitTest {

    @Tested
    TruModule truModule;
    @Injectable
    IDevice iDevice;
    @Injectable
    IXMLNetworkMessenger xmlNetworkMessenger;
    @Injectable
    Logger log;
    @Injectable
    ITruModuleProperties properties;
    @Injectable
    TruRatingMessageFactory truRatingMessageFactory;

    private XSD2TestFactory testFactory;

    @Before
    public void setUp() {
        properties = UnitTestProperties.getInstance(); // Set of test properties
        testFactory = new XSD2TestFactory(properties);
        truModule =  new TruModule(properties);

        setField(truModule, "xmlNetworkMessenger", xmlNetworkMessenger);
        setField(truModule, "iDevice", iDevice);
        setField(truModule, "log", log);
        setField(truModule, "truRatingMessageFactory", truRatingMessageFactory);
    }

    @Test
    public void doRatingTest() {
        new Expectations() {{
            xmlNetworkMessenger.getResponseQuestionFromService((Request)any);
            returns(testFactory.generateResponseForQuestion());
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns ("8");
            times = 1;
            truRatingMessageFactory.assembleQuestionRequest((ITruModuleProperties) any, (String)any);
            returns (testFactory.generateRequestForQuestion());
            times = 1;
        }};

        truModule.doRating();
        RequestRating ratingRecord = truModule.getCurrentRatingRecord() ;

        Assert.assertNotNull(ratingRecord.getDateTime());
        Assert.assertEquals(8, ratingRecord.getValue());
    }

    @Test
    public void doRatingNetworkServiceCallFails_RatingValueUnset() {
        new Expectations() {{
            xmlNetworkMessenger.getResponseQuestionFromService((Request)any);
            returns(null);
            times = 1;
            truRatingMessageFactory.assembleQuestionRequest((ITruModuleProperties) any, (String)any);
            returns (testFactory.generateRequestForQuestion());
            times = 1;
        }};

        truModule.doRating();
        RequestRating ratingRecord = truModule.getCurrentRatingRecord() ;
        Assert.assertEquals(ratingRecord.getValue(), TruModule.MODULE_ERROR);
    }

    @Test
    public void doRatingUserPressesCancelOnPedTest() {

        new Expectations() {{
            xmlNetworkMessenger.getResponseQuestionFromService((Request)any);
            returns(testFactory.generateResponseForQuestion());
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns (Short.toString(TruModule.USER_CANCELLED));
            times = 1;
        }};

        truModule.doRating();
        RequestRating requestRating = truModule.getCurrentRatingRecord();
        Assert.assertEquals(requestRating.getValue(), TruModule.USER_CANCELLED);
    }


    @Ignore //this needs thinking about, i.e. a way to delay rating until cancel request comes in
    public void doRatingUserPressesCancelOnPedTestBackgroundThread() {

        new Expectations() {{
            xmlNetworkMessenger.getResponseQuestionFromService((Request)any);
            returns(testFactory.generateResponseForQuestion());
            times = 1;
            iDevice.displayTruratingQuestionGetKeystroke((String[])any, (String)any, anyInt);
            returns (Short.toString(TruModule.USER_CANCELLED));
            times = 1;
        }};

        truModule.doRatingInBackground();
        RequestRating requestRating = truModule.getCurrentRatingRecord();
        Assert.assertEquals(requestRating.getValue(), TruModule.USER_CANCELLED);
    }

}


