/*
 * @(#)Log.java
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
 * 1.00 13 Jun 2013 Initial Version
 */

package com.truRating.truSharedData.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple log file writer, that is configurable with different levels of
 * logging, to allow more or less detail to be included in the log as
 * circumstances dictate.
 * 
 * @author Peter Salmon
 * 
 */
public class Log {

	private static String logFileName = "TR/Log/TRApp.log";
	private static int logLevel = 5;

	public static final int ERROR_LEVEL = 1;
	public static final int WARNING_LEVEL = 2;
	public static final int INFO_LEVEL = 3;
	public static final int DEBUG_LEVEL = 4;
	public static final int DEBUG_PLUS_LEVEL = 5; // just to push Trace up to 6!
	public static final int TRACE_LEVEL = 6;

	public static final String[] levelNames = { " NONE ", " ERROR ", " WARNING ", " INFO ",
			" DEBUG ", " DEBUG+ ", " TRACE " };

	public static void configureFrom(IProperties properties) {

		String logFolder = properties.getValue("logFolder", "TR/Log");
		if (logFolder != null)
			logFileName = logFolder + "/TRApp.log";

		File logfile = new File(logFileName);
		logfile.getParentFile().mkdirs();

		logLevel = properties.getIntValue("logLevel", 1);
	}

	public static int getLogLevel() {
		return logLevel;
	}

	public static void configure(int level, String fileName) {
		logLevel = level;
		logFileName = fileName;
	}

	/**
	 * Log at Error level
	 * 
	 * @param text
	 */
	public static void error(String text) {
		log(ERROR_LEVEL, text);
	}

	/**
	 * Log at Warning level
	 * 
	 * @param text
	 */
	public static void warning(String text) {
		log(WARNING_LEVEL, text);
	}

	/**
	 * Log at Info level
	 * 
	 * @param text
	 */
	public static void info(String text) {
		log(INFO_LEVEL, text);
	}

	/**
	 * Log at Debug level
	 * 
	 * @param text
	 */
	public static void debug(String text) {
		log(DEBUG_LEVEL, text);
	}

	/**
	 * Log at Debug level
	 * 
	 * @param text
	 */
	public static void trace(String text) {
		log(TRACE_LEVEL, text);
	}

	// Standard format string for date output
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void log(int level, String text) {
		try {
			if (level <= logLevel) {
				StringBuffer sb = new StringBuffer();
				sb.append(dateFormatter.format(new Date()));
				sb.append(levelNames[level]);
				sb.append(text);
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(logFileName, true)));
				out.println(sb.toString());
				out.close();
			}
		} catch (IOException e) {
			// oh noes!
		}
	}
}
