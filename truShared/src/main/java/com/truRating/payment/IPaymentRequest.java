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

	/**
	 * Unique (for this terminal) code identifying this transaction
	 */
	String getTransactionReference();

	/**
	 * Transaction type - goods / services / cashback etc
	 */
	 TransactionType getTransactionType();

	/**
	 * Tender Type (Cash, Swiped, Keyed, SmartCard, CustomerNotPresent, Contactless)
	 */
	TenderType getTenderType();

	/**
	 * The total transaction amount in the lowest denomination of the given currency
	 */
	long getTransactionValue();
	
	/**
	 * The amount requested for this tender forming part of the transaction, 
	 * in the lowest denomination of the given currency
	 */
	long getTenderValue();

	/**
	 * The amount of any cashback requested 
	 */
	long getCashbackAmount();

	/**
	 * Code to identify the product category
	 */
	String getProductCode();

	/**
	 * The currency code the transaction is to be conducted in
	 */
	int getCurrencyCode();

	/**
	 * Returns the ISO 639-1 code for language that this transaction is being
	 * conducted in Ideally this would be set by the cardholder's preferred
	 * language - as identified by the card although there may be a
	 * synchronisation issue if the card dialog cannot start until after the
	 * TruRating app has completed
	 */
	String getLanguageCode();

	/**
	 *	A string representation of the basket contents 
	 */
	String getBasket();
}