package com.trurating.network.xml;

import com.trurating.properties.ITruModuleProperties;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;
import com.trurating.xml.ratingResponse.RatingResponseJAXB;

public interface IXMLNetworkMessenger {

    /**
    Send the request for a question, if no response after timeOut seconds, will return null;
     */
	public abstract QuestionResponseJAXB getQuestionFromService(ITruModuleProperties properties, long transactionId) ;
	
    public abstract RatingResponseJAXB deliverRatingToService(RatingDeliveryJAXB rating) ;
    
    public abstract void close() ;
}