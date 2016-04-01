package com.truRating.network;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.truRating.properties.UnitTestProperties;
import com.truRating.truModule.TruModule;
import com.truRating.truModule.network.xml.XMLNetworkMessenger;
import com.truRating.truModule.payment.IPaymentResponse;
import com.truRating.truModule.properties.ITruModuleProperties;
import com.truRating.truModule.rating.Rating;
import com.truRating.truModule.xml.questionResponse.QuestionResponseJAXB;
import com.truRating.truModule.xml.ratingResponse.RatingResponseJAXB;
import com.truRating.util.IntegrationTestStartUp;

/**
 * Created by Paul on 11/03/2016.
 */
@RunWith(JMockit.class)
public class XMLNetworkMessengerIntegrationTest {

    @Tested
    TruModule truModule;
    @Injectable
    ITruModuleProperties properties;    
    @Injectable
    IPaymentResponse paymentResponse;
	
    @Before
    public void init() {
    	properties = UnitTestProperties.getInstance();
    }
    
    @Test
    public void TestgetQuestionFromService_checkForReturnedMessage() {
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();
        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        QuestionResponseJAXB questionResponse = xmlNetworkMessenger.getQuestionFromService(properties);
        Assert.assertNotNull(questionResponse);
    }

    @Test
    public void getQuestionFromService_sendAnIncorrectlyFormattedQuestion() {
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();
        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        QuestionResponseJAXB questionResponse = xmlNetworkMessenger.getQuestionFromService(properties);
        Assert.assertNotNull(questionResponse.getErrortext());
    }

    public static String now() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    return sdf.format(cal.getTime());
    }    
    
    @Test
    public void deliveryRatingToService_checkForReturnedMessage() {
        //will fail because the rating is not fully setup correctly
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();

        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        Rating rating = new Rating();
        rating.setValue(5);
        rating.setRatingDateTime(now());
        RatingResponseJAXB ratingResponseJAXB= xmlNetworkMessenger.deliveryRatingToService(rating, paymentResponse, properties);
        Assert.assertNull(ratingResponseJAXB);
    }

    //todo make a full rating for test purposes.. :)


}
