package com.trurating.properties;

import org.apache.log4j.Logger;

/**
 * A general loader of system properties.
 * This particular implementation uses java.util.Properties
 * but can we swapped out for any implementation
 * The ITruModuleProperties will vary across implementations and aims
 * to wrap to underlying properties implementation, so that only a set of getters
 * and setters are visible to partners.
 *
 */

public class TruModuleProperties implements ITruModuleProperties {

    private static Logger log = Logger.getLogger(TruModuleProperties.class);

    public TruModuleProperties() {
    }

    /**
     * MID - returns a unique idenitifier for the Store 
     */
    public String getMid() {
        return mid;
    }

    public void setMid(String value) {
        mid = value;
    }
    private String mid = "";

    
    /**
     * TID - A unique identifer for the till / lane
     */
    public String getTid() {
        return tid;
    }

    public void setTid(String value) {
        tid = value;
    }
    private String tid = "";

    /**
     * LanguageCode - the language the transaction is being conducted in
     */
    public String getLanguageCode() {
        return languageCode ;
    }

    public void setLanguageCode(String value) {
    	languageCode = value;
    }
    private String languageCode = "";

    
    /**
     * A flag to indicate whether the receipt text should 
     * be requested as part of the question request
     * (It always is in the rating delivery)  
     */
    public boolean getIncludeReceipt() {
        return includeReceiptInQuestionRequest;
    }

    public void setIncludeReceipt(boolean value) {
        includeReceiptInQuestionRequest = value;
    }
    boolean includeReceiptInQuestionRequest = true ;
    
    
    /**
     * A flag to indicate whether the acknowledgement text should 
     * be requested as part of the question request
     */
    public boolean getIncludeAcknowledgement() {
        return includeAcknowledgement;
    }

    public void setIncludeAcknowledgement(boolean value) {
        includeAcknowledgement = value;
    }
    boolean includeAcknowledgement = true ;


    /** 
     * The type of PED device in use  
     */
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String value) {
    	deviceType = value;
    }
    private String deviceType = "";

        
    /**
     * The number of display lines supported by the PED device
     */
    public int getDeviceLines() {
        return deviceLines;
    }

    public void setDeviceLines(int value) {
        deviceLines = value;
    }
    int deviceLines = 0;
    
    /**
     * The number of characters that may be printed on each line
     */
    public int getDeviceCpl() {
        return deviceCPL;
    }

    public void setDeviceCpl(int value) {
    	deviceCPL = value;
    }
    int deviceCPL = 0;
    
    /**
     * 
     */
    public String getDeviceFormat() {
        return deviceFormat;
    }

    public void setDeviceFormat(String value) {
    	deviceFormat = value;
    }
    private String deviceFormat = "";

    
    /**
     * A name and version of the firmware running on the PED device
     */
    public String getDeviceFirmware() {
        return deviceFirmware;
    }

    public void setDeviceFirmware(String value) {
    	deviceFirmware = value;
    }
    private String deviceFirmware = "";


    /** 
     * The type of font supported by the PED display (proportional or fixed width)
     */
    public String getDeviceFontType() {
        return deviceFontType;
    }

    public void setDeviceFontType(String value) {
    	deviceFontType = value;
    }
    private String deviceFontType = "";


    /**
     * 
     */
    public String getServerId() {
        return serverId;
    }

    public void setServerId(String value) {
    	serverId = value;
    }
    private String serverId = "";

     
    /**
     * The name and version of the payment application
     */
    public String getPpaFirmware() {
        return ppaFirmware;
    }

    public void setPpaFirmware(String value) {
    	ppaFirmware = value;
    }
    private String ppaFirmware = "";

    /**
     *  The IP address of the server running the truService application
     */
    public String getTruServiceIPAddress() {
        return ipAddress;
    }


    public void setTruServiceIPAddress(String value) {
    	ipAddress = value;
    }
    private String ipAddress = "";

    
   /**
     *  The port on the truService server that the truService application is listening on
    */
   public int getTruServiceSocketPortNumber() {
       return socketPortNumber;
   }

   public void setTruServiceSocketPortNumber(int value) {
   	socketPortNumber = value;
   }
   private int socketPortNumber = 0;

    
    /**
     * The timeout to use for socket connections to the truService application
     */
    public int getTruServiceSocketTimeoutInMilliSeconds() {
        return socketTimeoutInMilliSeconds;
    }


    public void setTruServiceSocketTimeoutInMilliSeconds(int value) {
    	socketTimeoutInMilliSeconds = value;
    }
    private int socketTimeoutInMilliSeconds = 500;
 }
