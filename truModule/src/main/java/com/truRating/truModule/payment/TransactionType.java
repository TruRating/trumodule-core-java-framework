/*
 * @(#)TransactionType.java
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

package com.truRating.truModule.payment;

import java.util.Vector;

public class TransactionType {

	/***************************************************************************
	 * Constructors
	 ***************************************************************************/

	String name;
	int value;
	static Vector AllValues = new Vector();

	/**
	 * Construct a new TransactionType.
	 */
	private TransactionType(String name, int value) {
		this.name = name;
		this.value = value;
		AllValues.add(this);
	}

	public int getIntValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public static TransactionType fromInt(int val) {
		for (int i = 0; i < AllValues.size(); i++)
			if (((TransactionType) AllValues.get(i)).getIntValue() == val)
				return (TransactionType) AllValues.get(i);
		return null;
	}

	public static TransactionType fromString(String val) {
		for (int i = 0; i < AllValues.size(); i++)
			if (((TransactionType) AllValues.get(i)).getName().equals(val))
				return (TransactionType) AllValues.get(i);
		return null;
	}

	/**
	 * Goods and services
	 */
	public static final TransactionType ISO_GOODSnSERVICES = new TransactionType(
			"Purchase", 0x00);

	/**
	 * Withdrawal/cash advance
	 */
	public static final TransactionType ISO_CASHONLY = new TransactionType(
			"Cashback", 0x01);

	/**
	 * Goods and services with Cash Disbursement
	 */
	public static final TransactionType ISO_CASHBACK = new TransactionType(
			"Purchase with cashback", 0x09);

	/**
	 * Returns
	 */
	public static final TransactionType ISO_REFUND = new TransactionType(
			"Refund", 0x20);

}
