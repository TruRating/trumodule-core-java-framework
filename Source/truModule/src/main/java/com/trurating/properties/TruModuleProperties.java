/*
 * @(#)TruModuleProperties.java
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

import org.apache.log4j.Logger;

/**
    Creates a facade of getters and setters over an implementation of a general properties loader
    -- feel to roll your own GeneralPropertiesLoader as appropriate
 */

public class TruModuleProperties extends GeneralPropertiesLoader implements ITruModuleProperties {

    private String mid = "";
    private String partnerId = "";
    private String tid = "";
    private String languageCode = "";
    boolean includeReceiptInQuestionRequest;
    boolean includeAcknowledgement;
    private String deviceType = "";
    private int deviceCPL;
    private String deviceFormat = "";
    private int deviceLines;
    private String deviceFontType = "";
    private int questionTimeout;
    private String ipAddress = "";
    private int socketTimeoutInMilliSeconds;
    private String serverId = "";
    private String deviceFirmware = "";
    private String ppaFirmware = "";

    private static Logger log = Logger.getLogger(TruModuleProperties.class);
    private String truServiceURL;

    public TruModuleProperties() {
        super(); //this call will load all properties loaded from all files as pointed by system "resources" JVM arg
        setMid(getProperty("mid"));
        setPartnerId(getProperty("partnerid"));
        setTid(getProperty("tid"));
        setLanguageCode("languageCode");
        setIncludeReceiptInQuestionRequest(getPropertyAsBoolean("includeReceipt"));
        setIncludeAcknowledgement(getPropertyAsBoolean("includeAcknowledgement"));
        setDeviceType(getProperty("device_Type"));
        setDeviceCPL(getPropertyAsInt("device_cpl"));
        setDeviceFormat(getProperty("device_format"));
        setDeviceFirmware(getProperty("device_firmware"));
        setDeviceFontType(getProperty("device_font_type"));
        setServerId(getProperty("123456789"));
        setPpaFirmware(getProperty("ppaFirmware"));
        setQuestionTimeout(getPropertyAsInt("timeoutFromQuestion"));
        setSocketTimeoutInMilliSeconds(getPropertyAsInt("socket_timeout"));
        setTruServiceURL(getProperty("service_URL"));
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }


    public String getPartnerId() {
        return partnerId;
    }


    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }


    public String getTid() {
        return tid;
    }


    public void setTid(String tid) {
        this.tid = tid;
    }


    public String getLanguageCode() {
        return languageCode;
    }


    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }


    public boolean isIncludeReceiptInQuestionRequest() {
        return includeReceiptInQuestionRequest;
    }


    public void setIncludeReceiptInQuestionRequest(boolean includeReceiptInQuestionRequest) {
        this.includeReceiptInQuestionRequest = includeReceiptInQuestionRequest;
    }


    public boolean isIncludeAcknowledgement() {
        return includeAcknowledgement;
    }


    public void setIncludeAcknowledgement(boolean includeAcknowledgement) {
        this.includeAcknowledgement = includeAcknowledgement;
    }


    public String getDeviceType() {
        return deviceType;
    }


    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    public int getDeviceCPL() {
        return deviceCPL;
    }


    public void setDeviceCPL(int deviceCPL) {
        this.deviceCPL = deviceCPL;
    }


    public String getDeviceFormat() {
        return deviceFormat;
    }


    public void setDeviceFormat(String deviceFormat) {
        this.deviceFormat = deviceFormat;
    }


    public int getDeviceLines() {
        return deviceLines;
    }


    public void setDeviceLines(int deviceLines) {
        this.deviceLines = deviceLines;
    }


    public String getDeviceFontType() {
        return deviceFontType;
    }


    public void setDeviceFontType(String deviceFontType) {
        this.deviceFontType = deviceFontType;
    }


    public int getQuestionTimeout() {
        return questionTimeout;
    }


    public void setQuestionTimeout(int questionTimeout) {
        this.questionTimeout = questionTimeout;
    }


    public String getIpAddress() {
        return ipAddress;
    }


    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    public int getSocketTimeoutInMilliSeconds() {
        return socketTimeoutInMilliSeconds;
    }


    public void setSocketTimeoutInMilliSeconds(int socketTimeoutInMilliSeconds) {
        this.socketTimeoutInMilliSeconds = socketTimeoutInMilliSeconds;
    }


    public String getServerId() {
        return serverId;
    }


    public void setServerId(String serverId) {
        this.serverId = serverId;
    }


    public String getDeviceFirmware() {
        return deviceFirmware;
    }


    public void setDeviceFirmware(String deviceFirmware) {
        this.deviceFirmware = deviceFirmware;
    }


    public String getPpaFirmware() {
        return ppaFirmware;
    }


    public void setPpaFirmware(String ppaFirmware) {
        this.ppaFirmware = ppaFirmware;
    }


    public String getTruServiceURL() {
        return truServiceURL;
    }


    public void setTruServiceURL(String truServiceURL) {
        this.truServiceURL = truServiceURL;
    }
}
