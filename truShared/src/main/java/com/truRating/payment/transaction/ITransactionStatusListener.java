/*
 * @(#)ITransactionStatusListener.java
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
 * 1.00 14 Jun 2013 Initial Version
 */

package com.trurating.payment.transaction;

import com.trurating.payment.IPaymentResponse;

/**
 * A listener from the calling routine to be informed when a transaction is
 * complete
 * 
 * @author Peter Salmon 
 * 
 */
public interface ITransactionStatusListener {

	/**
	 * Indication of progress so far
	 *            - The ITransactionContext that holds the request and response
	 *            instances for this transaction
	 */
	void statusUpdate(int status, String description);

	/**
	 * Instruction to print the receipts when it is time to do so
	 * 
	 * @param context
	 *            - The ITransactionContext that holds the request and response
	 *            instances for this transaction
	 */
//	void printReceipts(ITransactionContext context) throws Exception;
//			throws ReceiptsNotPrintedException;

	/**
	 * Callback that is called when a transaction is complete with the result of
	 * the transaction
	 */
	void transactionComplete(IPaymentResponse paymentResponse);
}
