/*
 * @(#)TransactionResult.java
 *
 * Copyright (c) 2016 truRating Limited. All Rights Reserved.
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



package com.trurating.payment;

import java.util.Vector;

/**
 * Static enumeration of values that indicate the progress of a payment
 * transaction
 * 
 * @author Peter Salmon 
 * 
 */
public class TransactionResult {

	String name;
	int value;
	static Vector AllValues = new Vector();

	public TransactionResult(String name, int value) {
		this.name = name;
		this.value = value;
		AllValues.add(this);
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return name;
	}

	public static TransactionResult fromInt(int val) {
		for (int i = 0; i < AllValues.size(); i++)
			if (((TransactionResult) AllValues.get(i)).getValue() == val)
				return (TransactionResult) AllValues.get(i);
		return null;
	}

	public static TransactionResult fromString(String val) {
		for (int i = 0; i < AllValues.size(); i++)
			if (((TransactionResult) AllValues.get(i)).getName().equals(val))
				return (TransactionResult) AllValues.get(i);
		return null;
	}

	public static TransactionResult UNKNOWN = new TransactionResult(
			"Unknown", -2);
	public static TransactionResult CANCELLED = new TransactionResult(
			"Cancelled", -1);
	public static TransactionResult DECLINED = new TransactionResult(
			"Declined", 0);
	public static TransactionResult DECLINED_ONLINE = new TransactionResult(
			"DeclinedOnline", 2);
	public static TransactionResult APPROVED = new TransactionResult(
			"Approved", 1);
	public static TransactionResult APPROVED_ONLINE = new TransactionResult(
			"ApprovedOnline", 3);
	public static TransactionResult ERROR = new TransactionResult(
			"Error", 4);
}
