package com.trurating.network;

import static mockit.Deencapsulation.setField;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.trurating.TruModule;
import com.trurating.network.xml.TruRatingMessageFactory;
import com.trurating.network.xml.XMLNetworkMessenger;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.properties.UnitTestProperties;
import com.trurating.util.IntegrationTestStartUp;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;
import com.trurating.xml.ratingResponse.RatingResponseJAXB;

/**
 * Created by Paul on 11/03/2016.
 */
@RunWith(JMockit.class)
public class XMLNetworkMessengerIntegrationTest {

    @Tested
    TruModule truModule;
    @Injectable
    ITruModuleProperties properties;
	
    @Before
    public void init() {
    	properties = UnitTestProperties.getInstance();
    }

    @Test
    public void TestgetQuestionFromService_checkForReturnedMessage() {
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();

        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        QuestionResponseJAXB questionResponse = xmlNetworkMessenger.getQuestionFromService(properties, 12345);
        Assert.assertNotNull(questionResponse);
    }
    
    @Test
    public void deliveryRatingToService_checkForReturnedMessage() {
        //will fail because the rating is not fully setup correctly
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();

        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        // Open the connection
        QuestionResponseJAXB questionResponse = xmlNetworkMessenger.getQuestionFromService(properties, 12345);
        
        RatingDeliveryJAXB ratingRecord = new TruRatingMessageFactory().createRatingRecord(properties);
        ratingRecord.getRating().setValue((short) 5);
        
        // Deliver the result
        RatingResponseJAXB ratingResponseJAXB= xmlNetworkMessenger.deliverRatingToService(ratingRecord);
        Assert.assertNotNull(ratingResponseJAXB);
    }

    //todo make a full rating for test purposes.. :)


}
