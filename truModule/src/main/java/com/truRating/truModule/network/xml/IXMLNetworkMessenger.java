package com.truRating.truModule.network.xml;

import com.truRating.truModule.payment.IPaymentResponse;
import com.truRating.truModule.properties.ITruModuleProperties;
import com.truRating.truModule.rating.Rating;
import com.truRating.truModule.xml.questionResponse.QuestionResponseJAXB;
import com.truRating.truModule.xml.ratingResponse.RatingResponseJAXB;

/**
 * Created by Paul on 08/03/2016.
 */
public interface IXMLNetworkMessenger {

    QuestionResponseJAXB getQuestionFromService(ITruModuleProperties properties);
    RatingResponseJAXB deliveryRatingToService(Rating iRating, IPaymentResponse paymentResponse, ITruModuleProperties properties);
}
