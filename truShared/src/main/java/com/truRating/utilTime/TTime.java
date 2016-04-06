/*
 * @(#)Time.java
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

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A representation of time, that brings together the way that times are handled
 * in payment applications, trurating, and in the Java date class.
 * 
 * @author Peter Salmon
 * 
 */
public class TTime {

	private Logger log = Logger.getLogger(TTime.class);
	private Calendar dateValue;

	public TTime() {
		setDateValue(new java.util.Date());
	}

	public TTime(Calendar calendarVal) {
		this.dateValue = calendarVal;
	}

	public TTime(int intVal) {
		setFromIntStrValue(Integer.toString(intVal));
	}

	public TTime(String intStrValue) {
		if (intStrValue.indexOf(':') > -1) {
			dateValue = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			try {
				dateValue.setTime(sdf.parse(intStrValue));
			} catch (ParseException e) {
				log.error("Invalid time received: " + String.valueOf(intStrValue));
				setFromIntStrValue("000000");
			}
		} else
			setFromIntStrValue(intStrValue);
	}

	private void setFromIntStrValue(String intStrValue) {

		dateValue = Calendar.getInstance();

		if (intStrValue.length() == 5)
			intStrValue = "0" + intStrValue;

		dateValue.set(Calendar.HOUR, 0);
		if (intStrValue.length() > 1)
			dateValue.set(Calendar.HOUR, Integer.parseInt(intStrValue.substring(0, 2)));

		dateValue.set(Calendar.MINUTE, 0);
		if (intStrValue.length() > 3)
			dateValue.set(Calendar.MINUTE, Integer.parseInt(intStrValue.substring(2, 4)));

		dateValue.set(Calendar.SECOND, 0);
		if (intStrValue.length() > 5)
			dateValue.set(Calendar.SECOND, Integer.parseInt(intStrValue.substring(4, 6)));
	}

	public int intValue() {
		String siv = this.toString("HHmmss");
		int rv = Integer.valueOf(siv).intValue();
		return rv;
	}

	public java.util.Date getDateValue() {
		return dateValue.getTime();
	}

	public void setDateValue(java.util.Date dateVal) {
		this.dateValue = Calendar.getInstance();
		this.dateValue.clear();
		this.dateValue.setTime(dateVal);
	}

	public Calendar getCalendarValue() {
		return dateValue;
	}

	public Calendar getGMTCalendarValue() {
		return convertToGmt(dateValue);
	}

	public void setCalendarValue(Calendar val) {
		this.dateValue = val;
	}

	public TimeZone getTimeZone() {
		return dateValue.getTimeZone();
	}

	public String getGMTOffset() {
		long offsetSecs = dateValue.getTimeZone().getOffset(getDateValue().getTime());
		if (offsetSecs == 0)
			return "Z";
		long offset = offsetSecs / 36000;
		boolean minus = (offset < 0);
		if (minus)
			offset = 0 - offset;
		String sval = ("0000" + String.valueOf(offset));
		String rv1 = sval.substring(sval.length() - 4);
		String rv = ((minus) ? "-" : "+") + rv1.substring(0, 2) + ":" + rv1.substring(2, 4);
		return rv;
	}

	public Calendar convertToGmt(Calendar cal) {

		java.util.Date date = cal.getTime();
		TimeZone tz = cal.getTimeZone();

		log.debug("input calendar has date [" + date + "]");

		// Returns the number of milliseconds since January 1, 1970, 00:00:00
		// GMT
		long msFromEpochGmt = date.getTime();

		// gives you the current offset in ms from GMT at the current date
		int offsetFromUTC = tz.getOffset(msFromEpochGmt);
		log.debug("offset is " + offsetFromUTC);

		// create a new calendar in GMT timezone, set to this date and add the
		// offset
		Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		gmtCal.setTime(date);
		gmtCal.add(Calendar.MILLISECOND, offsetFromUTC);

		log.debug("Created GMT cal with date [" + gmtCal.getTime() + "]");

		return gmtCal;
	}

	public String toString() {
		return toString("HH:mm:ss");
	}

	public String toString(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(dateValue.getTime());
	}

	/**
	 * @param nbrSecs	
	 *            Add number of seconds to the current time var
	 */	
	public Calendar add(int nbrSecs){		
		dateValue.add(Calendar.SECOND, nbrSecs);
		return dateValue;
	}
	/**
	 * @param nbrSecs	
	 *            subtract number of seconds to the current time var 
	 */	
	public Calendar subtract(int nbrSecs){		
		dateValue.add(Calendar.SECOND, -nbrSecs);
		return dateValue;
	}

	/*
	 * main
	 */
	public static void main(String[] args) {
		TTime time = new TTime("12:34:56");
		System.out.println(time);
		System.out.println(time.getGMTOffset());

		time.setCalendarValue(time.getGMTCalendarValue());
		System.out.println(time);
		System.out.println(time.getGMTOffset());
	}
}
