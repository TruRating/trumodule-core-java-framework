/*
 * @(#)IPaymentResponse.java
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
 * 1.00 25 Jun 2013 Initial Version
 */

package com.trurating.payment;


/**
 * @author Peter Salmon
 * 
 *         The details of the final response to a card payment request.
 */
public interface IPaymentResponse {

	/**
	 * Unique merchant number
	 */
	String getMerchantNumber();

	/**
	 * Unique terminal number
	 */
	String getTerminalId();

    String getOperatorId();

    /**
	 * Transaction number, unique to the point of service
	 */
	long getTransactionId();

    /**
	 * POS application's transaction reference
	 */
	String getTransactionReference();

	/**
	 * Transaction date
	 */
	String getTransactionDate();

	/**
	 * Transaction time
	 */
	String getTransactionTime();

	/**
	 * Error code
	 */
	int getErrorCode();

	/**
	 * Error code
	 */
	void setErrorCode(int value);

	/**
	 * Error Message associated with Error Code
	 */
	String getErrorMessage();

	/**
	 * Error Message associated with Error Code
	 */
	void setErrorMessage(String value);

	/**
	 * Transaction type (Sales, Refund, Cash, etc.)
	 */
	TransactionType getTransactionType();

	/**
	 * Tender Type (Cash, Swiped, Keyed, SmartCard, CustomerNotPresent, Contactless)
	 */
	TenderType getTenderType();
	
	/**
	 * Transaction amount in lowest denomination
	 */
	long getTransactionAmount();

	/**
	 * Cashback element of transaction amount
	 */
	long getCashbackAmount();

	/**
	 * Currency code for transaction amount
	 */
	int getCurrencyCode();

    String getCardHashType();

    String getCardHashData();

	TransactionResult getTransactionResult();
	
	
	/**
	 * Flag to indicate whether the transaction was approved
	 * 
	 */
	boolean isApproved();

	/**
	 * Flag to indicate whether the transaction was cancelled
	 * 
	 */
	boolean isCancelled();
}