package com.trurating.network;

import com.trurating.service.v200.xml.Request;
import com.trurating.service.v200.xml.RequestRating;
import com.trurating.service.v200.xml.Response;
import mockit.integration.junit4.JMockit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.trurating.network.xml.TruRatingMessageFactory;
import com.trurating.network.xml.XMLNetworkMessenger;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.properties.UnitTestProperties;
import com.trurating.util.IntegrationTestStartUp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Paul on 11/03/2016.
 */
@RunWith(JMockit.class)
public class XMLNetworkMessengerIntegrationTest {

    ITruModuleProperties properties;

    @Before
    public void init() {
    	properties = UnitTestProperties.getInstance();
    }

    @Test
    public void deliveryRatingToService_checkForReturnedMessage() throws IOException {

        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();

        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        TruRatingMessageFactory truRatingMessageFactory = new TruRatingMessageFactory();
        String sessionId= Long.toString(new Date().getTime());
        Request request = truRatingMessageFactory.assembleQuestionRequest(properties, sessionId);

        Response questionResponseFromService = xmlNetworkMessenger.getResponseFromService(request, properties);
        Assert.assertNotNull(questionResponseFromService);
        
        Request deliveryRequest = new TruRatingMessageFactory().assembleRatingsDeliveryRequest(properties, sessionId);
        RequestRating requestRating = new RequestRating();
        requestRating.setResponseTimeMs(5000);
        requestRating.setRfc1766("en-GB");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        requestRating.setValue((short)5);
        requestRating.setDateTime(sdf.format(new Date()));

        deliveryRequest.setRating(requestRating);
        // Deliver the result
        Response ratingDeliveryResponseFromService = xmlNetworkMessenger.getResponseFromService(deliveryRequest, properties);
        Assert.assertNotNull(ratingDeliveryResponseFromService);
    }
}

//            URL url = new URL("http://tru-sand-service-v200.trurating.com/api/servicemessage ");
