package com.truRating.truModule.properties;

public interface ITruModuleProperties {

    String getMid();
    String getTid();
    String getUid();
    String getLanguageCode();
    String getIncludeReceipt();
    String getIncludeAcknowledgement();
    String getDeviceType();
    String getDevicenLines();
    String getDeviceCpl();
    String getDeviceFormat();
    String getDeviceFirmware();
    String getDeviceFontType();
    String getServerId();
    String getPpaFirmware();
    String getTruServiceIPAddress();
    int getTruServiceSocketTimeoutInMilliSeconds();
    int getTruServiceSocketPortNumber();
}