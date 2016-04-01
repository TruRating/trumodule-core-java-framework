/*
 * @(#)Properties.java
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A dictionary of arbitrary settings used during transaction processing. May be
 * set from the contents of a typical key=value formatted properties file.
 * 
 * @author Peter Salmon
 * 
 */
public class Properties implements IProperties {

	private HashMap properties = new HashMap();

	public Properties() {
	}

	public Properties(String filename) throws TRException {
		try {
			read(filename);
		} catch (Exception e) {
			Log.error("Exception occurred reading Properties file: " + filename
					+ ". " + e.toString());
		}
	}

	/**
	 * Initialise a property dictionary from a string 
	 * where the string is made up of many properties delimited by the given delimiter
	 * @param source
	 * @param delimiter
	 * @throws TRException
	 */
	public Properties(String source, String delimiter) {
		String[] properties = StringUtilities.split(source, delimiter) ;
		for (int i = 0; i < properties.length; i++) 
			readProperty(properties[i]); 
	}

	/* (non-Javadoc)
	 * @see com.trurating.common.IProperties#containsKey(java.lang.String)
	 */
	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see com.trurating.common.IProperties#getValue(java.lang.String, java.lang.String)
	 */
	public String getValue(String key, String defaultValue) {
		if (properties.containsKey(key))
			return (String) properties.get(key);
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see com.trurating.common.IProperties#setValue(java.lang.String, java.lang.String)
	 */
	public void setValue(String key, String value) {
		properties.put(key, value);
	}

	// Read one key=value setting
	private void readProperty(String property) {
		// System.out.println(line);
		// Log.debug("Property read from file:" + line);
		if ((property.length() > 0)
				&& Character.isLetterOrDigit(property.charAt(0))) {
			int pos = property.indexOf('=');
			if (pos > 0)
				setValue(property.substring(0, pos).trim(),
						property.substring(pos + 1).trim());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.trurating.common.IProperties#read(java.lang.String)
	 */
	public void read(String filename) throws TRException {
		try {
			// System.out.println(System.getProperty("user.dir"));
			BufferedReader inputStream = FileUtilities.getBufferedReader(
					Properties.class, filename);

			String line = inputStream.readLine();
			while (line != null) {
				readProperty(line);
				line = inputStream.readLine();
			}
		} catch (FileNotFoundException e) {
			String msg = "Failed to find properties file : " + filename;
			Log.error(msg);
		} catch (IOException e) {
			String msg = "Failed to read properties file : " + filename;
			Log.error(msg);
			e.printStackTrace();
			throw new TRException(msg, e);
		}
	}

	/* (non-Javadoc)
	 * @see com.trurating.common.IProperties#write(java.lang.String)
	 */
	public void write(String filename) {
		try {
			PrintWriter outputStream = new PrintWriter(new FileWriter(filename));

			Iterator it = properties.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = (String) properties.get(key);
				outputStream.println(key + "=" + value);
			}
			outputStream.close();

		} catch (IOException e) {
			Log.error("Failed to write properties file:" + filename + "\n"
					+ e.toString());
		}
	}

	/* (non-Javadoc)
	 * @see com.trurating.common.IProperties#getIntValue(java.lang.String, int)
	 */
	public int getIntValue(String key, int defaultValue) {
		int rv = defaultValue;
		String value = getValue(key, null);
		if (value != null)
			rv = Integer.parseInt(value);
		return rv;
	}

	/* (non-Javadoc)
	 * @see com.trurating.common.IProperties#getBoolValue(java.lang.String, boolean)
	 */
	public boolean getBoolValue(String key, boolean defaultValue) {
		boolean rv = defaultValue;
		String value = getValue(key, null);
		if (value != null) {
			rv = false;
			value = value.toLowerCase().trim();
			if (value.equals("true") || value.equals("yes")
					|| value.equals("y"))
				rv = true;
		}
		return rv;
	}
}
