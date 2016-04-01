/*
 * @(#)ITransactionContext.java
 *
 * Copyright (c) 2013 truRating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * truRating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with truRating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT 
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

/*
 * VERSION HISTORY:
 *
 * 1.00 20 Jun 2013 Initial Version
 */

package com.truRating.truSharedData.payment.transaction;

import com.truRating.truModule.payment.IPaymentRequest;
import com.truRating.truModule.payment.IPaymentResponse;

/**
 * @author Peter Salmon
 * 
 *         A collection of records that form part of a payment transaction
 */
public interface ITransactionContext {

	/**
	 * The transaction request
	 */
	IPaymentRequest getRequest();

	/**
	 * The transaction response
	 */
	IPaymentResponse getResponse();

	/**
	 * Set the response to the payment request in the transaction context
	 * 
	 * @param response
	 */
	void setResponse(IPaymentResponse response);

	/**
	 * The name of the person operating the till
	 * 
	 * @return String
	 */
	String getOperator();

	/**
	 * Set the name of the person operating the till
	 */
	void setOperator(String operator);

	/**
	 * Retrieve the value of the field with the given name
	 */
	Object getFieldValue(String field);
}
