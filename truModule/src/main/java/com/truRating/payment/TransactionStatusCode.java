/*
 * @(#)TransactionResultCode.java
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
 * 1.00 01/06/2013 Initial Version
 */

package com.trurating.payment;

import java.util.Vector;

/**
 * TransactionResultCode
 */
public final class TransactionStatusCode {

	/***************************************************************************
	 * Constructors
	 ***************************************************************************/

	int value;
	String name;
	String friendly;
	private boolean approved;
	private boolean cancelled;
	private boolean error;
	static Vector allValues = new Vector();

	/**
	 * Construct a new TransactionResultCode.
	 */
	private TransactionStatusCode(String name, int value, String friendly) {
		this.name = name;
		this.value = value;
		this.friendly = friendly;
		this.approved = false;
		this.cancelled = false;
		allValues.add(this);
	}

	/**
	 * Construct a new AttendantActionCodes, indicating whether this code
	 * identifies a transaction approved result
	 */
	private TransactionStatusCode(String name, int value, String friendly, boolean approvedCode,
			boolean cancelledCode) {
		this.name = name;
		this.value = value;
		this.friendly = friendly;
		allValues.add(this);
		this.approved = approvedCode;
		this.cancelled = cancelledCode;
		this.error = (value == EMV_T_ERROR);
	}

	public int getIntValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the approved flag
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * @return the approved flag
	 */
	public boolean isDeclined() {
		return ((value == EMV_T_DECLINED_ONLINE) ||
				(value == EMV_T_DECLINED_OFFLINE)) ;
	}

	/**
	 * @return the cancelled flag
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @return the error flag
	 */
	public boolean isAnError() {
		return error;
	}

	public static TransactionStatusCode fromInt(int amount) {
		for (int i = 0; i < allValues.size(); i++)
			if (((TransactionStatusCode) allValues.get(i)).getIntValue() == amount)
				return (TransactionStatusCode) allValues.get(i);
		return VOID;
	}

	public static TransactionStatusCode fromString(String val) {
		for (int i = 0; i < allValues.size(); i++)
			if (((TransactionStatusCode) allValues.get(i)).getName().equals(val))
				return (TransactionStatusCode) allValues.get(i);
		return null;
	}

	public String toString() {
		return friendly;
	}

	/***************************************************************************
	 * truRating additional transaction status codes
	 ***************************************************************************/

	/** Start of checkout */
	public static final int TR_START_OF_CHECKOUT = 10001;

	/** End of of checkout - start of payment */
	public static final int TR_END_OF_CHECKOUT = 10002;

	/***************************************************************************
	 * Account selection codes
	 ***************************************************************************/

	/** ACCOUNT SELECTION CODE - Entry cancelled */
	public static final int EMV_C_CANCEL = -1; // Equal to
												// STSCardHolderAccountSelectionProgress.CANCEL
	/** ACCOUNT SELECTION CODE - An error occurred */
	public static final int EMV_C_ERROR = -99;

	/***************************************************************************
	 * Transaction Result Codes
	 ***************************************************************************/

	/**
	 * TRANSACTION RESULT CODE - No application selected
	 */
	public static final int EMV_T_VOID = 0;
	public static final TransactionStatusCode VOID = new TransactionStatusCode("EMV_T_VOID",
			EMV_T_VOID, "Void");

	/**
	 * TRANSACTION RESULT CODE - Approved by on-line host
	 */
	public static final int EMV_T_APPROVED_ONLINE = 1;
	public static final TransactionStatusCode APPROVED_ONLINE = new TransactionStatusCode(
			"EMV_T_APPROVED_ONLINE", EMV_T_APPROVED_ONLINE, "Approved Online", true, false);

	/**
	 * TRANSACTION RESULT CODE - Approved but not by on-line host
	 */
	public static final int EMV_T_APPROVED_OFFLINE = 2;
	public static final TransactionStatusCode APPROVED_OFFLINE = new TransactionStatusCode(
			"EMV_T_APPROVED_OFFLINE", EMV_T_APPROVED_OFFLINE, "Approved Offline", true, false);

	/**
	 * TRANSACTION RESULT CODE - Approved by external manual procedure
	 */
	public static final int EMV_T_APPROVED_MANUAL = 3;
	public static final TransactionStatusCode APPROVED_MANUAL = new TransactionStatusCode(
			"EMV_T_APPROVED_MANUAL", EMV_T_APPROVED_MANUAL, "Approved Manual", true, false);

	/**
	 * TRANSACTION RESULT CODE - Declined by on-line host
	 */
	public static final int EMV_T_DECLINED_ONLINE = 4;
	public static final TransactionStatusCode DECLINED_ONLINE = new TransactionStatusCode(
			"EMV_T_DECLINED_ONLINE", EMV_T_DECLINED_ONLINE, "Declined Online");

	/**
	 * TRANSACTION RESULT CODE - Declined but not by on-line host
	 */
	public static final int EMV_T_DECLINED_OFFLINE = 5;
	public static final TransactionStatusCode DECLINED_OFFLINE = new TransactionStatusCode(
			"EMV_T_DECLINED_OFFLINE", EMV_T_DECLINED_OFFLINE, "Declined Offline");

	/**
	 * TRANSACTION RESULT CODE - Waiting for signature verification and manual
	 * authorisation
	 */
	public static final int EMV_T_WAITING_MAN_CVM = 6;
	public static final TransactionStatusCode WAITING_MAN_CVM = new TransactionStatusCode(
			"EMV_T_WAITING_MAN_CVM", EMV_T_WAITING_MAN_CVM, "Waiting Man Cvm");

	/**
	 * TRANSACTION RESULT CODE - Waiting for manual authorisation (initiated by
	 * on-line host)
	 */
	public static final int EMV_T_WAITING_MANUAL = 7;
	public static final TransactionStatusCode WAITING_MANUAL = new TransactionStatusCode(
			"EMV_T_WAITING_MANUAL", EMV_T_WAITING_MANUAL, "Waiting Manual");

	/**
	 * TRANSACTION RESULT CODE - Waiting for signature verification
	 */
	public static final int EMV_T_WAITING_CVM = 8;
	public static final TransactionStatusCode WAITING_CVM = new TransactionStatusCode(
			"EMV_T_WAITING_CVM", EMV_T_WAITING_CVM, "Waiting Cvm");

	/**
	 * TRANSACTION RESULT CODE - Transaction was cancelled
	 */
	public static final int EMV_T_CANCELLED = 9;
	public static final TransactionStatusCode CANCELLED = new TransactionStatusCode(
			"EMV_T_CANCELLED", EMV_T_CANCELLED, "Cancelled", false, true);

	/**
	 * TRANSACTION RESULT CODE - Waiting for transaction amount(s) to be
	 * confirmed
	 */
	public static final int EMV_T_WAITING_AMOUNT = 10;
	public static final TransactionStatusCode WAITING_AMOUNT = new TransactionStatusCode(
			"EMV_T_WAITING_AMOUNT", EMV_T_WAITING_AMOUNT, "Waiting Amount");

	/**
	 * TRANSACTION RESULT CODE - Waiting for manual authorisation (initiated by
	 * card)
	 */
	public static final int EMV_T_WAITING_MANUAL_CARD = 11;
	public static final TransactionStatusCode WAITING_MANUAL_CARD = new TransactionStatusCode(
			"EMV_T_WAITING_MANUAL_CARD", EMV_T_WAITING_MANUAL_CARD, "Waiting Manual Card");

	/**
	 * TRANSACTION RESULT CODE - Waiting for confirmation of cash back
	 */
	public static final int EMV_T_WAITING_CASHBACK = 12;
	public static final TransactionStatusCode WAITING_CASHBACK = new TransactionStatusCode(
			"EMV_T_WAITING_CASHBACK", EMV_T_WAITING_CASHBACK, "Waiting Cashback");

	/** TRANSACTION RESULT CODE - Pre-EMV transaction type has timed out */
	public static final int EMV_T_TIMED_OUT = 13;
	public static final TransactionStatusCode TIMED_OUT = new TransactionStatusCode(
			"EMV_T_TIMED_OUT", EMV_T_TIMED_OUT, "Timed Out", false, true);
	/**
	 * TRANSACTION RESULT CODE - Pre-EMV transaction type needs to fall back to
	 * EMV
	 */
	public static final int EMV_T_WAITING_EMV = 14;
	public static final TransactionStatusCode WAITING_EMV = new TransactionStatusCode(
			"EMV_T_WAITING_EMV", EMV_T_WAITING_EMV, "Waiting Emv");

	/**
	 * TRANSACTION RESULT CODE - Waiting for data to be exchanged with card
	 * reader
	 */
	public static final int EMV_T_WAITING_APDU = 20;
	public static final TransactionStatusCode WAITING_APDU = new TransactionStatusCode(
			"EMV_T_WAITING_APDU", EMV_T_WAITING_APDU, "Waiting Apdu");

	/**
	 * TRANSACTION RESULT CODE - Waiting for PIN verification to be completed
	 */
	public static final int EMV_T_WAITING_PIN = 21;
	public static final TransactionStatusCode WAITING_PIN = new TransactionStatusCode(
			"EMV_T_WAITING_PIN", EMV_T_WAITING_PIN, "Waiting Pin");

	/**
	 * TRANSACTION RESULT CODE - Waiting for card payment system to perform hot
	 * card check
	 */
	public static final int EMV_T_WAITING_HOTCARD = 30;
	public static final TransactionStatusCode WAITING_HOTCARD = new TransactionStatusCode(
			"EMV_T_WAITING_HOTCARD", EMV_T_WAITING_HOTCARD, "Waiting Hotcard");

	/**
	 * TRANSACTION RESULT CODE - Waiting for card payment system to perform
	 * on-line authorisation
	 */
	public static final int EMV_T_WAITING_OLA = 31;
	public static final TransactionStatusCode WAITING_OLA = new TransactionStatusCode(
			"EMV_T_WAITING_OLA", EMV_T_WAITING_OLA, "Waiting Ola");

	/**
	 * TRANSACTION RESULT CODE - Waiting for card payment system to perform
	 * on-line reversal
	 */
	public static final int EMV_T_WAITING_REVERSAL = 32;
	public static final TransactionStatusCode WAITING_REVERSAL = new TransactionStatusCode(
			"EMV_T_WAITING_REVERSAL", EMV_T_WAITING_REVERSAL, "Waiting Reversal");

	/**
	 * TRANSACTION RESULT CODE - Waiting for card payment system to perform
	 * on-line referral
	 */
	public static final int EMV_T_WAITING_REFERRAL = 33;
	public static final TransactionStatusCode WAITING_REFERRAL = new TransactionStatusCode(
			"EMV_T_WAITING_REFERRAL", EMV_T_WAITING_REFERRAL, "Waiting Referral");

	/**
	 * TRANSACTION RESULT CODE - Card Holder Application has started
	 */
	public static final int EMV_T_START_APPSEL = 34;
	public static final TransactionStatusCode START_APPSEL = new TransactionStatusCode(
			"EMV_T_START_APPSEL", EMV_T_START_APPSEL, "Start Appsel");

	/**
	 * TRANSACTION RESULT CODE - Card Holder Application has ended
	 */
	public static final int EMV_T_END_APPSEL = 35;
	public static final TransactionStatusCode END_APPSEL = new TransactionStatusCode(
			"EMV_T_END_APPSEL", EMV_T_END_APPSEL, "End Appsel");

	/** TRANSACTION RESULT CODE - Pin Progress has started */
	public static final int EMV_T_START_PINPROGRESS = 36;
	public static final TransactionStatusCode START_PINPROGRES = new TransactionStatusCode(
			"EMV_T_START_PINPROGRESS", EMV_T_START_PINPROGRESS, "Start Pinprogress");
	/** TRANSACTION RESULT CODE - Pin Progress has ended */
	public static final int EMV_T_END_PINPROGRESS = 37;
	public static final TransactionStatusCode END_PINPROGRESS = new TransactionStatusCode(
			"EMV_T_END_PINPROGRESS", EMV_T_END_PINPROGRESS, "End Pinprogress");
	/** TRANSACTION RESULT CODE - Application data has been read from the card */
	public static final int EMV_T_END_READ_APPLICATION_DATA = 40;
	public static final TransactionStatusCode END_READ_APPLICATION_DATA = new TransactionStatusCode(
			"EMV_T_END_READ_APPLICATION_DATA", EMV_T_END_READ_APPLICATION_DATA,
			"End Read Application Data");
	/** TRANSACTION RESULT CODE - Cardholder account selection has started */
	public static final int EMV_T_START_ACCSEL = 41;
	public static final TransactionStatusCode START_ACCSEL = new TransactionStatusCode(
			"EMV_T_START_ACCSEL", EMV_T_START_ACCSEL, "Start Accsel");
	/** TRANSACTION RESULT CODE - Cardholder account selection has ended */
	public static final int EMV_T_END_ACCSEL = 42;
	public static final TransactionStatusCode END_ACCSEL = new TransactionStatusCode(
			"EMV_T_END_ACCSEL", EMV_T_END_ACCSEL, "End Accsel");
	/**
	 * TRANSACTION RESULT CODE - Waiting for card payment system to retrieve
	 * last amount authorised for this card
	 */
	public static final int EMV_T_WAITING_LAST_AMOUNT_AUTHORISED = 43;
	public static final TransactionStatusCode WAITING_LAST_AMOUNT_AUTHORISED = new TransactionStatusCode(
			"EMV_T_WAITING_LAST_AMOUNT_AUTHORISED", EMV_T_WAITING_LAST_AMOUNT_AUTHORISED,
			"Waiting Last Amount Authorised");

	/**
	 * TRANSACTION RESULT CODE - An error has occurred, <code>Error</code>
	 * property should be examined
	 */
	public static final int EMV_T_ERROR = 50;
	public static final TransactionStatusCode ERROR = new TransactionStatusCode("EMV_T_ERROR",
			EMV_T_ERROR, "Error", false, true);

	/**
	 * TRANSACTION RESULT CODE - Transaction has not started: Context is idle
	 */
	public static final int EMV_T_STARTED = 97;
	public static final TransactionStatusCode STARTED = new TransactionStatusCode("EMV_T_STARTED",
			EMV_T_STARTED, "started");

	/**
	 * TRANSACTION RESULT CODE - Transaction has not started: Context is idle
	 */
	public static final int EMV_T_ENDED = 98;
	public static final TransactionStatusCode ENDED = new TransactionStatusCode("EMV_T_ENDED",
			EMV_T_ENDED, "Transaction complete");

	/**
	 * TRANSACTION RESULT CODE - Transaction has not started: Context is idle
	 */
	public static final int EMV_T_NOTSTARTED = 99;
	public static final TransactionStatusCode NOTSTARTED = new TransactionStatusCode(
			"EMV_T_NOTSTARTED", EMV_T_NOTSTARTED, "Notstarted");
}
