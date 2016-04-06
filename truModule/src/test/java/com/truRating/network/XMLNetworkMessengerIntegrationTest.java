package com.trurating.network;

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
    public void getQuestionFromService_sendAnIncorrectlyFormattedQuestion() {
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();
        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        QuestionResponseJAXB questionResponse = xmlNetworkMessenger.getQuestionFromService(properties, 12345);
        Assert.assertNotNull(questionResponse.getErrortext());
    }

    public static String now() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return sdf.format(cal.getTime());
    }    
    
    @Test
    public void deliveryRatingToService_checkForReturnedMessage() {
        //will fail because the rating is not fully setup correctly
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();

        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        RatingDeliveryJAXB ratingRecord = new TruRatingMessageFactory().createRatingRecord(properties);
        ratingRecord.getRating().setValue((short) 5);
        RatingResponseJAXB ratingResponseJAXB= xmlNetworkMessenger.deliverRatingToService(ratingRecord);
        Assert.assertNull(ratingResponseJAXB);
    }

    //todo make a full rating for test purposes.. :)


}
