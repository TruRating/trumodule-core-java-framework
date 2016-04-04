/*
 * @(#)IPaymentRequest.java
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

package com.trurating.payment;

/**
 * @author Peter Salmon
 *         <p>
 *         The transaction details that are required for a request for EFT
 *         payment.
 */
public interface IPaymentRequest {

	long getTransactionValue();
	
	long getTenderValue();

	/**
	 * Initial cashback amount - ignored by this demonstrator
	 *
	 * @return
	 */
	long getCashbackAmount();

	/**
	 * Code to identify the product category
	 *
	 * @return
	 */
	String getProductCode();

	String getTransactionReference();

	void setTransactionReference(String transactionReference);

	/**
	 * Transaction type - goods / services / cashback etc
	 *
	 * @return
	 */
//	ITransactionType getTransactionType();

	/**
	 * The currency code is hardwired to 826 (GBP) for demonstration purposes
	 *
	 * @return
	 */
	int getCurrencyCode();

	void setCurrencyCode(int code);

	/**
	 * The currency symbol
	 *
	 * @return
	 */
	String getCurrencySymbol();

	/**
	 * The currency exponent is set to 2 - to turn GBP into the lowest
	 * denomination - pence
	 *
	 * @return
	 */
	int getCurrencyExponent();

	/**
	 * Returns the ISO 639-1 code for language that this transaction is being
	 * conducted in Ideally this would be set by the cardholder's preferred
	 * language - as identified by the card although there may be a
	 * synchronisation issue if the card dialog cannot start until after the
	 * TruRating app has completed
	 *
	 * @return
	 */
	String getLanguageCode();

	/**
	 * Returns the ID of the terminal conducting this transaction
	 *
	 * @return
	 */
	String getTerminalId();

	/**
	 * Returns the ID of the attendant who is conducting the transaction
	 *
	 * @return
	 */
	String getOperatorId();

	void setOperatorId(String id);

	/**
	 * Set a value in the Properties map Used by the truRating plugin to
	 * influence the output on the receipt
	 */
	void setProperty(String key, String value);

	/**
	 * Return a value from the Properties map
	 *
	 * @return String
	 */
	String getProperty(String key);

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

	TransactionType getTransactionType();

	String getBasket();

	void setBasket(String basket);

	String getProduct();

	void setProduct(String product);

	String getOperatorID();

	void setOperatorID(String operatorID);
}