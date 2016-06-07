package com.trurating.properties;

/**
 * Created by Paul Russell on 07/06/2016.
 */

//this interface will tie together all the various versions of the truModuleProperties loader
public interface ITruModuleProperties {

    String getMid();

    String getPartnerId();

    String getTid();

    String getLanguageCode();

    boolean isIncludeReceiptInQuestionRequest();

    boolean isIncludeAcknowledgement();

    String getDeviceType();

    int getDeviceCPL();

    String getDeviceFormat();

    int getDeviceLines();

    String getDeviceFontType();

    int getQuestionTimeout();

    int getSocketTimeoutInMilliSeconds();

    String getServerId();

    String getDeviceFirmware();

    String getPpaFirmware();

    String getTruServiceURL();
}
