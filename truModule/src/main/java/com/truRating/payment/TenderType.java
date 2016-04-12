/*
 * @class TenderType.java
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
 * 
 * @version 14/11/2015 initial version
 * 
 * @author Peter Wong
 * 
 * @details Defines the different types of tenderType.
 *          The type of payment used to pay for the transaction
 */

package com.trurating.payment;

import java.util.Vector;

public class TenderType {

	/***************************************************************************
	 * Constructors
	 ***************************************************************************/

	String name;
	int value;
	static Vector AllValues = new Vector();

	/**
	 * Construct a new TenderType.
	 */
	private TenderType(String name, int value) {
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

	public static TenderType fromInt(int val) {
		for (int i = 0; i < AllValues.size(); i++)
			if (((TenderType) AllValues.get(i)).getIntValue() == val)
				return (TenderType) AllValues.get(i);
		return null;
	}

	public static TenderType fromString(String val) {
		for (int i = 0; i < AllValues.size(); i++)
			if (((TenderType) AllValues.get(i)).getName().equals(val))
				return (TenderType) AllValues.get(i);
		return null;
	}

	/**
	 * Cash (there is no value for cash in ISO8583 so we just use 00)
	 */
	public static final TenderType CASH = new TenderType(
			"Cash", 0x00);

	/**
	 * keyed entry, (value taken from F22)
	 */
	public static final TenderType KEYED = new TenderType(
			"Keyed", 0x01);
	/**
	 * Mag Stripe, swiped, (value taken from F22)
	 */
	public static final TenderType SWIPED = new TenderType(
			"Swiped", 0x02);

	/**
	 * chipped card with PIN, (value taken from F22)
	 */
	public static final TenderType SMARTCARD = new TenderType(
			"SmartCard", 0x05);

	/**
	 * Contactless, (value taken from F22)
	 */
	public static final TenderType CONTACTLESS = new TenderType(
			"Contactless", 0x07);
	
	/**
	 * Customer not present, usually a MOTO (value taken from F25)
	 */
	public static final TenderType CUSTOMERNOTPRESENT = new TenderType(
			"CustomerNotPresent", 0x08);
	
	/**
	 * UNKNOWN,
	 */
	public static final TenderType UNKNOWN = new TenderType(
			"Unknown", 0xFF);
}
