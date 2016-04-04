package com.trurating.network.xml;

import com.trurating.payment.IPaymentRequest;
import com.trurating.payment.IPaymentResponse;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.rating.Rating;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.ratingResponse.RatingResponseJAXB;

/**
 * Created by Paul on 08/03/2016.
 */
public interface IXMLNetworkMessenger {

    QuestionResponseJAXB getQuestionFromService(ITruModuleProperties properties, String transactionId);
    RatingResponseJAXB deliveryRatingToService(Rating iRating, String transactionId, IPaymentResponse paymentResponse, ITruModuleProperties properties);
}
