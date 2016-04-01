package com.truRating.truModule.device;/*
 * @(#)IDevice.java
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
 * 1.00 01/06/2013 Initial Version
 */

/**
 * @author Peter
 * 
 *         An interface that describes the behaviour that the truRating
 *         application requires of a PIN entry device.
 */

public interface IDevice {

	public static final int OK = 0x0D;
	public static final int ESC = 0x1B;
	public static final int F1 = 0xF1;
	public static final int F2 = 0xF2;
	public static final int BS = 0x7F;
	public static final int CANCEL = -1;
	public static final int TIMEOUT = -2;

	/**
	 * Get the devices serial number
	 * 
	 * @return
	 */
	String getSerialNo();

	/**
	 * Access the technical details about the device such as the screen size
	 * 
	 * @return
	 */
	DeviceCapabilities getDeviceCapabilities();

	/**
	 * Determine whether a payment card has been inserted into the device
	 * 
	 * @return true if a card is in place, false if not.
	 */
	boolean isCardIn();

	/**
	 * Get keypad input, expecting that the function keys and command keys will
	 * be enabled.
	 * 
	 * @return
	 */
	int getKey(int timeout);

	/**
	 * Display the standard Welcome message on the display of the PIN entry device
	 * 
	 */
	void displayWelcome() ;
	
	/**
	 * Display the standard Please Wait message on the display of the PIN entry device
	 * 
	 */
	void displayPleaseWait() ;
	
	/**
	 * Display a message on the display of the PIN entry device
	 * 
	 * @param string
	 *            - the message to display
	 */
	void displayMessage(String string);

	/**
	 * Display a message on the display of the PIN entry
	 * device, and wait for a keypress acknowledgement 
	 * 
	 * @param string
	 *            - the message to display
	 */
	void displayMessageWaitForKey(String string, int timeout);

	/**
	 * Display the truRating secure prompt (ie a certified one that allows
	 * numeric keystrokes) and wait for a single keystroke.
	 * 
	 */
	String displaySecurePromptGetKeystroke(String[] prompts,
												  String promptText, int timeout);

	/**
	 * Display the truRating question (ie an arbitary one that allows numeric
	 * keystrokes) and wait for a single keystroke.
	 * 
	 * @return
	 */
	String displayTruratingQuestionGetKeystroke(String[] prompts, String promptText,
													   int timeout);

	/**
	 * Cancel a command requesting key input
	 */
	void cancelInput();
	
	
	/**
	 * Returns a component that is capable of adding a truRating acknowledgment
	 * to the receipt
	 * 
	 * @return
	 */
//	IReceiptProcessor getReceiptProcessor(IPaymentRequest paymentRequest);
}
