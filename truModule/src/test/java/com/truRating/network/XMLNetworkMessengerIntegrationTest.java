package com.truRating.network;

import com.truRating.truModule.network.xml.XMLNetworkMessenger;
import com.truRating.truModule.payment.TruModulePaymentResponse;
import com.truRating.truModule.rating.Rating;
import com.truRating.truSharedData.properties.ProgramProperties;
import com.truRating.truSharedData.utilTime.TDateTime;
import com.truRating.truModule.xml.questionResponse.QuestionResponseJAXB;
import com.truRating.truModule.xml.ratingResponse.RatingResponseJAXB;
import com.truRating.util.IntegrationTestStartUp;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by Paul on 11/03/2016.
 */
public class XMLNetworkMessengerIntegrationTest {

    @Test
    public void TestgetQuestionFromService_checkForReturnedMessage() {
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();
        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        QuestionResponseJAXB questionResponse = xmlNetworkMessenger.getQuestionFromService();
        Assert.assertNotNull(questionResponse);
    }

    @Test
    public void getQuestionFromService_sendAnIncorrectlyFormattedQuestion() {
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();
        ProgramProperties.getInstance().load(new File("C:\\dev\\source\\trumodule-core-java\\source\\truModule\\src\\main\\resources\\testProps\\testPropertiesIncorrectlyFormatted.txt"));
        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        QuestionResponseJAXB questionResponse = xmlNetworkMessenger.getQuestionFromService();
        Assert.assertNotNull(questionResponse.getErrortext());
    }

    @Test
    public void deliveryRatingToService_checkForReturnedMessage() {
        //will fail because the rating is not fully setup correctly
        IntegrationTestStartUp.setupLog4J();
        IntegrationTestStartUp.startup();

        XMLNetworkMessenger xmlNetworkMessenger = new XMLNetworkMessenger();
        Rating rating = new Rating();
        rating.setValue(5);
        rating.setRatingDateTime(new TDateTime().toString());
        RatingResponseJAXB ratingResponseJAXB= xmlNetworkMessenger.deliveryRatingToService(rating, new TruModulePaymentResponse());
        Assert.assertNull(ratingResponseJAXB);
    }

    //todo make a full rating for test purposes.. :)


}
