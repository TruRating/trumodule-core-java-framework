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
        return "http://localhost:31415/api/servicemessage";
//        return "http://tru-sand-service-v200.trurating.com/api/servicemessage";
    }

    public int getSocketTimeoutInMilliSeconds() {
        return 3000;
    }

    public int getDwellTimeExtend() {
        return 0;
    }
}
