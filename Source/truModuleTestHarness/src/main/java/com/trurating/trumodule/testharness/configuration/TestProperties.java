package com.trurating.trumodule.testharness.configuration;

import com.trurating.properties.TruModuleProperties;

/**
 * Created by Paul on 2016. :)
 */
public class TestProperties extends TruModuleProperties {

    private static TestProperties INSTANCE=null;

    public TestProperties() {}

    public static synchronized TestProperties getInstance() {
        if (INSTANCE==null) {
            return INSTANCE= new TestProperties();
        }
        return INSTANCE;
    }

    public String getPartnerId() {
        return "1";
    }

    public String getMid() {
        return "1234567";
    }

    public String getTid() {
        return "00000tR2";
    }

    public String getLanguageCode() {
        return "en-GB";
    }

    public boolean isIncludeReceiptInQuestionRequest() {
        return true;
    }

    public boolean isIncludeAcknowledgement() {
        return true;
    }

    public String getDeviceType() {
        return "IPP350";
    }

    public int getDeviceLines() {
        return 4;
    }

    public int getDeviceCPL() {
        return 16;
    }

    public String getDeviceFormat() {
        return "TEXT_CENTERED";
    }

    public String getDeviceFirmware() {
        return "Windows 10";
    }

    public String getDeviceFontType() {
        return "PROPORTIONAL";
    }

	public int getQuestionTimeout() {
		return 50000;
	}
    
    public String getServerId() {
        return "123456789";
    }

    public String getPpaFirmware() {
        return "MCM4.2";
    }

    public String getTruServiceURL() {
///        return "tru-sand-service-fis.cloudapp.net";
        return "http://localhost:47851/api/servicemessage";
//        return "tru-sand-service-trurating.cloudapp.net";
//        return "13.95.156.19";
//        return "127.0.0.1";
    }

    public int getSocketTimeoutInMilliSeconds() {
        return 3000;
    }
}
