/*
 * @(#)StringUtilities.java
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

package com.trurating.util;

import java.util.ArrayList;

/**
 * A set of general purpose string functions
 * 
 * @author Peter Salmon
 * 
 */
public class StringUtilities {

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

	public static void main(String[] args) {
		String[] sa = StringUtilities.wordWrap("trurating: Please rate the food/drinks from 0-9 or clear",16);
        System.out.println();
    }
}
