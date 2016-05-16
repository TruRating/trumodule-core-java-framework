/*
 * @(#)IXMLNetworkMessenger.java
 *
 * Copyright (c) 2016 trurating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * trurating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with trurating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT 
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.trurating.network.xml;

import com.trurating.properties.ITruModuleProperties;
import trurating.service.v121.xml.questionResponse.QuestionResponseJAXB;
import trurating.service.v121.xml.ratingDelivery.RatingDeliveryJAXB;
import trurating.service.v121.xml.ratingResponse.RatingResponseJAXB;

public interface IXMLNetworkMessenger {

    /**
    Send the questionRequest for a question, if no questionResponse after timeOut seconds, will return null;
     */
	QuestionResponseJAXB getQuestionFromService(ITruModuleProperties properties, long transactionId) ;
	
    RatingResponseJAXB deliverRatingToService(RatingDeliveryJAXB rating) ;
    
    void close() ;
}