/*
 * @(#)IPaymentApplication.java
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
 * 1.00 25 Sep 2013 Initial Version
 */

package com.trurating.payment;

import com.trurating.device.IDevice;
import com.trurating.payment.transaction.ITransactionStatusListener;

/**
 * @author Peter Salmon
 * 
 *         Defines the expected behaviour of a payment application that is
 *         connected to a PIN entry device for Chip and PIN transactions.
 */
public interface IPaymentApplication {

	/**
	 * Flag to indicate whether trurating has been enabled, and whether it has been activated
	 */
	boolean hasTruratingEnabledAndActive();

	/**
	 * Call to ask payment app to run a background exchange with the trurating host API
	 * to make sure that the LDS is up to date
	 */
	 void updateTrurating();

	boolean initialise();

	/**
	 * New event from POS to payment app to indicate that checkout is starting
	 */
	void startTransaction(String tillOperator, String salesPerson);

	void startTransaction(String tillOperator, String salesPerson, String txnID);

	/**
	 * New event from POS to payment app to indicate that checkout is starting
	 */
	void startCheckout();

	/**
	*	Delivers the details of one item scanned from the basket to the trurating app.
	*	Note that a copy of the basket item provided is made during this call, so the
	*	instance provided can be safely deleted if necessary.
	*/
	// public abstract void ScanItem(TRBasketItem item);	
	
	
	/**
	 * New event from POS to payment app to indicate that checkout has finished
	 * Note that the payment may not be by card - so this is not a request for
	 * payment yet
	 */
	void endCheckout(IPaymentRequest paymentDetails);

	/**
	 * Process a payment as a Chip and PIN transaction using Emvelink The
	 * Plug-in applications will be called at various strategic points around
	 * the processing, as defined by the TransactionStatus enumerated list
	 * @return 
	 * 
	 */
	IPaymentResponse requestPayment(IPaymentRequest paymentRequest, ITransactionStatusListener transactionStatusListener);

	/**
	 * End a transaction (if one's running).
	 */
	void endTransaction(IPaymentResponse paymentResponse);

	/**
	 * Return a message describing the last error
	 */
	String getErrorMessage();

	/**
	 * Shut down the payment application
	 */
	void shutdown();

	IDevice getDevice();
}