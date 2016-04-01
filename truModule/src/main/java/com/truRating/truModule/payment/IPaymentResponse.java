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

package com.truRating.truModule.payment;


/**
 * @author Peter Salmon
 * 
 *         The details of the final response to a card payment request.
 */
public interface IPaymentResponse {

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

    /**
	 * Transaction number, unique to point of service
	 */
	int getTransactionNumber();

	/**
	 * Transaction number, unique to point of service
	 */
	void setTransactionNumber(int value);

	/**
	 * Transaction date
	 */
	String getTransactionDate();

	/**
	 * Transaction date
	 */
	void setTransactionDate(String value);

	/**
	 * Transaction time
	 */
	String getTransactionTime();

	/**
	 * Transaction time
	 */
	void setTransactionTime(String value);

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
//	ITransactionType getTransactionType();

	/**
	 * Transaction type (Sales, Refund, Cash, etc.)
	 */
//	void setTransactionType(ITransactionType value);

	/**
	 * Transaction amount in lowest denomination
	 */
	long getTransactionAmount();

	/**
	 * Transaction amount in lowest denomination
	 */
	void setTransactionAmount(long value);

	/**
	 * Cashback element of transaction amount
	 */
	long getCashbackAmount();

	/**
	 * Cashback element of transaction amount
	 */
	void setCashbackAmount(long value);

	/**
	 * Currency code for transaction amount
	 */
	int getCurrencyCode();

	/**
	 * Currency code for transaction amount
	 */
	void setCurrencyCode(int value);

	/**
	 * Unique merchant number
	 */
	String getMerchantNumber();

	/**
	 * Unique merchant number
	 */
	void setMerchantNumber(String value);

	/**
	 * Unique terminal number
	 */
	String getTerminalId();

	/**
	 * Unique terminal number
	 */
	void setTerminalId(String value);
	
	/**
	 * Tender Type (Cash, Swiped, Keyed, SmartCard, CustomerNotPresent, Contactless)
	 */
	/**
	 * @return tenderType
	 */
	TenderType getTenderType();
	
	/**
	 * @param[in] tenderType
	 */
    void setTenderType(TenderType value);

	String getCardScheme();

	void setCardScheme(String value);

    void setTransactionIsApproved();

    void setOperatorId(String operatorId);

    String getOperatorId();

    void setCardHashType(String cdh1);

    String getCardHashType();

    void setCardHashData(String s);

    String getCardHashData();

	void setResult(TransactionStatusCode cancelled);

	TransactionStatusCode getResult();
}