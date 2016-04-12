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

package com.trurating.common;

//import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
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

	/**
	 * Break some text into an array of strings, word wrapping before the
	 * maxWidth value is reached
	 *
	 * @param qt
	 * @return
	 */
    public static String[] wordWrap(String qt, int maxTermWidth, int margin) {

        final int MAX_TERM_WIDTH=maxTermWidth;
        final int LEFT_MARGIN=4;
        final int MAX_LINE_WIDTH=MAX_TERM_WIDTH-LEFT_MARGIN;
        final int MAX_TERM_HEIGHT = 8;
        final String[] outputList = new String[MAX_TERM_HEIGHT];


        StringTokenizer s = new StringTokenizer(qt, " ");
        String sentence = "";
        int currentLine=0;
        while (s.hasMoreElements()) {
            String word = s.nextToken();
            if (sentence.length()+word.length()<MAX_LINE_WIDTH) {
                sentence+= " " + word;
            } else {
                outputList[currentLine++] = sentence;
                sentence="";
                sentence+= " " + word;
            }
        }

        outputList[currentLine++] = sentence;

        for (int i =0; i<outputList.length; i++) {
//            outputList[i]=StringUtils.center(outputList[i], MAX_TERM_WIDTH);
        }


        StringBuffer aBlankLine= new StringBuffer();
        for (int i=0; i<MAX_TERM_WIDTH; i++) {
            aBlankLine.append(" ");
        }

        Vector v = new Vector();
        for (int i=0; i<outputList.length; i++) {
            if (outputList[i]!=null) v.add(outputList[i]);
        }

        if (v.size() < MAX_TERM_HEIGHT) {
            int addedLines = (MAX_TERM_HEIGHT-v.size())/2;
            for (int i=0; i<addedLines; i++) {
                v.insertElementAt(aBlankLine.toString(), i);
            }
        }

        String[] revisedOutPutList = new String[v.size()];
        for (int i =0; i<revisedOutPutList.length; i++) {
            revisedOutPutList[i]= v.get(i).toString();
        }
        return revisedOutPutList;
    }

	public static void main(String[] args) {
		String[] sa = StringUtilities.wordWrap("truRating: Please rate the food/drinks from 0-9 or clear",16);
        System.out.println();
    }
}
