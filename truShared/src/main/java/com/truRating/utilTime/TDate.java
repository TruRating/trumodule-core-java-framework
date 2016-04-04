/*
 * @(#)DateTime.java
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

package com.trurating.utilTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A representation of dates, that brings together the way that dates are
 * handled in payment applications, truRating, and in Java.
 * 
 * @author Peter Salmon
 * 
 */
public class TDate {

	private Calendar dateValue;

	public TDate() {
		dateValue = Calendar.getInstance();
	}

	public TDate(Calendar calendarVal) {
		this.dateValue = calendarVal;
	}

	public TDate(int value) {
		int yr = 0;
		int mn = 0;
		int dy = 0;
		String sDateTime = Integer.toString(value);
		int len = sDateTime.length();

		if (sDateTime.length() == 8) {
			yr = Integer.parseInt(sDateTime.substring(0, 4));
			mn = Integer.parseInt(sDateTime.substring(4, 6));
			dy = Integer.parseInt(sDateTime.substring(6, 8));
		}
		else {
			if (sDateTime.length() > 1)
				yr = 2000 + Integer.parseInt(sDateTime.substring(0, 2));
			if (sDateTime.length() > 3)
				mn = Integer.parseInt(sDateTime.substring(2, 4));
			if (sDateTime.length() > 5)
				dy = Integer.parseInt(sDateTime.substring(4, 6));
		}

		dateValue = Calendar.getInstance();
		dateValue.clear();
		dateValue.set(yr, mn - 1, dy);
	}

	public Calendar getDateValue() {
		return dateValue;
	}

	/**
	 * Add a given time to this date and return a Time that is a DateTime
	 * 
	 * @param time
	 * @return
	 */
	public TTime getDateTimeValue(TTime time) {
		Calendar cal = Calendar.getInstance();
		cal.clear();

		Calendar tm = time.getCalendarValue();
		cal.set(dateValue.get(Calendar.YEAR), dateValue.get(Calendar.MONTH),
				dateValue.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.HOUR),
				tm.get(Calendar.MINUTE), tm.get(Calendar.SECOND));

		TTime rv = new TTime();
		rv.setCalendarValue(cal);
		return rv;
	}

	public int intValue() {
		String siv = this.toString("yyyyMMdd");
		int rv = Integer.valueOf(siv).intValue();
		return rv;
	}

	public String toString() {
		return toString("yyyy-MM-dd");
	}

	public String toString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(dateValue.getTime());
	}

	public static void main(String[] args) {
		TDate date = new TDate(140501);
		System.out.println(date);
	}
}
