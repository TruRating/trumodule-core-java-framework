/*
 * @(#)FileUtilities.java
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * General purpose file functions
 * 
 * @author Peter Salmon
 * 
 */

public class FileUtilities {

	/**
	 * Check to see if a file exists
	 * 
	 * @param originalFileName
	 * @return
	 */
	public static boolean fileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * Move and/ or rename a file
	 * 
	 * @param oldName
	 * @param newName
	 */
	public static boolean moveFile(String oldName, String newName) {
		boolean rv = false;
		try {
			File file = new File(oldName);
			rv = file.renameTo(new File(newName));
		} catch (SecurityException e) {
			Log.error("Security exception occurred while renaming a file:"
					+ e.toString());
		}
		return rv;
	}

	/**
	 * Delete a file
	 * 
	 * @param filename
	 */
	public static boolean deleteFile(String filename) {
		boolean rv = false;
		try {
			File file = new File(filename);
			rv = file.delete();
		} catch (SecurityException e) {
			Log.error("Security exception occurred while deleting a file:"
					+ e.toString());
		}
		return rv;
	}

	/**
	 * Append the contents of the source text file to the destination text file
	 * 
	 * @param originalFileName
	 * @param tempFileName
	 */
	public static boolean appendTextFile(String destFileName,
			String sourceFileName) {
		boolean rv = false;
		BufferedReader inputStream = null;
		PrintWriter outputStream = null;
		try {
			inputStream = new BufferedReader(new FileReader(sourceFileName));
			outputStream = new PrintWriter(new FileWriter(destFileName, true));

			String line = inputStream.readLine();
			while (line != null) {
				// TODO CHECK this doesn't add another newline
				outputStream.println(line);
				line = inputStream.readLine();
			}
			rv = true;
		} catch (FileNotFoundException e) {
			// There's nothing to append - so nothing to do
			rv = true;
		} catch (IOException e) {
			Log.error("IOException occurred while opening a file to append:"
					+ e.toString());
			e.printStackTrace();
		} finally {

			// Make sure both files are closed
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (outputStream != null)
				outputStream.close();
		}
		return rv;
	}

	/**
	 * Checks to see if the file exists on disk, and if it does not then tries
	 * to open it in the jar resources. Note that the bufferedReader returned
	 * must be closed by the caller.
	 * 
	 * @param resourceFileName
	 * @return
	 */
	public static BufferedReader getBufferedReader(Class callingClass,
			String resourceFileName) {

		BufferedReader inputStream = null;
		try {
			if (fileExists(resourceFileName))
				inputStream = new BufferedReader(new FileReader(
						resourceFileName));
			else {
				String fn = (resourceFileName.startsWith("/") ? resourceFileName
						: ("/" + resourceFileName));
				InputStream is = callingClass.getResourceAsStream(fn);
				inputStream = new BufferedReader(new InputStreamReader(is));
			}
		} catch (FileNotFoundException e) {

			Log.error("Failed to open resource file " + resourceFileName + "\n"
					+ e.toString());
			e.printStackTrace();
		}
		return inputStream;
	}
}
