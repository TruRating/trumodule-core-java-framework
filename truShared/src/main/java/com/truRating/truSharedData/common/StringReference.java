/*
 * @(#)StringReference.java
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
 * 1.00 26 Jun 2013 Initial Version
 */

package com.truRating.truSharedData.common;

/**
 * An object that represents a reference to a string enabling a calling function
 * to change the string it has been passed
 * 
 * @author Peter Salmon 
 * 
 */
public class StringReference {
	String value;

	public StringReference() {
		this.value = "";
	}

	public StringReference(String value) {
		this.value = value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public String toString() {
		return this.value;
	}

	public boolean equals(Object anObject) {
		return this.value.equals(anObject.toString());
	}

	public char charAt(int index) {
		return this.value.charAt(index);
	}

	public int indexOf(char ch) {
		return value.indexOf(ch);
	}

	public int indexOf(char ch, int index) {
		return value.indexOf(ch, index);
	}

	public int indexOf(String str) {
		return value.indexOf(str);
	}

	public int length() {
		return this.value.length();
	}

	public String substring(int beginIndex) {
		return value.substring(beginIndex);
	}

	public String substring(int beginIndex, int endIndex) {
		return value.substring(beginIndex, endIndex);
	}

	public String trim() {
		return value.trim();
	}
}
