package com.trurating.network.xml;

import com.trurating.payment.IPaymentResponse;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.rating.Rating;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.ratingResponse.RatingResponseJAXB;

public interface IXMLNetworkMessenger {

    /**
    Send the request for a question, if no response after timeOut seconds, will return null;
     */
	public abstract QuestionResponseJAXB getQuestionFromService(ITruModuleProperties properties, String transactionId) ;
	
    public abstract RatingResponseJAXB deliveryRatingToService(ITruModuleProperties properties, String transactionId, Rating rating, IPaymentResponse paymentResponse) ;
}