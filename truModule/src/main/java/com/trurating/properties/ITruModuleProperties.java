/*
 * @(#)ITruModuleProperties.java
 *
 * Copyright (c) 2016 trurating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * trurating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with trurating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT 
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.trurating.properties;

public interface ITruModuleProperties {

    /**
     * MID - returns a unique idenitifier for the Store 
     */
    String getMid();

    /**
     * TID - A unique identifer for the till / lane
     */
    String getTid();

    /**
     * LanguageCode - the language the transaction is being conducted in
     */
    String getLanguageCode();

    /**
     * A flag to indicate whether the receipt text should 
     * be requested as part of the question request
     * (It always is in the rating delivery)  
     */
    boolean getIncludeReceipt();

    /**
     * A flag to indicate whether the acknowledgement text should 
     * be requested as part of the question request
     */
    boolean getIncludeAcknowledgement();

    /** 
     * The type of PED device in use  
     */
    String getDeviceType();

    /**
     * The number of display lines supported by the PED device
     */
    int getDeviceLines();

    /**
     * The number of characters that may be printed on each line
     */
    int getDeviceCpl();

    /**
     * 
     */
    String getDeviceFormat();

    /**
     * A name and version of the firmware running on the PED device
     */
    String getDeviceFirmware();

    /** 
     * The type of font supported by the PED display (proportional or fixed width)
     */
    String getDeviceFontType();

    /**
     * The question timeout in milliseconds
     * @return
     */
    int getQuestionTimeout() ;    
    
    /**
     * 
     */
    String getServerId();

    /**
     * The name and version of the payment application
     */
    String getPpaFirmware();

    /**
     *  The IP address of the server running the truService application
     */
    String getTruServiceIPAddress();

    /**
     *  The port on the truService server that the truService application is listening on
    */
    int getTruServiceSocketPortNumber();
    
    /**
     * The timeout to use for socket connections to the truService application
     */
    int getTruServiceSocketTimeoutInMilliSeconds();
}