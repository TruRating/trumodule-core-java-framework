package com.trurating.properties;

public interface ITruModuleProperties {

    String getMid();
    String getTid();
    String getLanguageCode();
    boolean getIncludeReceipt();
    boolean getIncludeAcknowledgement();
    String getDeviceType();
    int getDeviceLines();
    int getDeviceCpl();
    String getDeviceFormat();
    String getDeviceFirmware();
    String getDeviceFontType();
    String getServerId();
    String getPpaFirmware();
    String getTruServiceIPAddress();
    int getTruServiceSocketTimeoutInMilliSeconds();
    int getTruServiceSocketPortNumber();
}