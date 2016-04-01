/*
 * @(#)StringUtilities.java
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
 * 1.00 27 Jun 2013 Initial Version
 */

package com.truRating.truSharedData.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * A set of general purpose string functions
 * 
 * @author Peter Salmon
 * 
 */
public class StringUtilities {

	public static String[] split(String source, String delimiter) {

		Vector rv = new Vector();

		int start = 0;
		int dlen = delimiter.length();
		int pos = source.indexOf(delimiter);

		while (pos != -1) {
			rv.add(source.substring(start, pos));
			start = pos + dlen;
			pos = source.indexOf(delimiter, start);
		}
		rv.add(source.substring(start));
		return (String[]) rv.toArray(new String[rv.size()]);
	}

	final protected static char[] hexArray = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Convert a given byte array into a hex string
	 * 
	 * @param b
	 * @return
	 */
	public static String byteArrayToHexString(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Convert a string formatted as 2 byte hex to a byte array
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(
					s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Read a file and return the contents as a string
	 * 
	 * @param filename
	 * @return
	 */
	public static String readFile(String filename) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * Break some text into an array of strings, word wrapping before the
	 * maxWidth value is reached
	 * 
	 * @param questionText
	 * @return
	 */
	public static String[] wordWrap(String qt, int maxWidth) {
		ArrayList rv = new ArrayList();
		String padding = "                           ";

		// Keep breaking at the next natural line ending until all the text
		// is displayed
		while ((qt != null) && (qt.length() > 0)) {

			// Work out the line break
			String line = "";
			int charPos = maxWidth;
			if (qt.length() <= charPos) {
				line = qt;
				qt = "";
			} else {
				for (int i = charPos; i > 0; i--) {
					if (qt.charAt(i) == ' ') {
						charPos = i;
						break;
					}
				}
				line = (qt.substring(0, charPos) + padding).substring(0, maxWidth);
				charPos++;
				qt = (charPos < qt.length()) ? qt.substring(charPos).trim() : "";
			}

			rv.add(line);
		}
		return (String[]) rv.toArray(new String[rv.size()]);
	}
}
