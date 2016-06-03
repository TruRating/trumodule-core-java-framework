package com.trurating.trumodule.testharness.configuration;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.trurating.properties.ITruModuleProperties;

/**
 * Created by Paul on 2016. :)
 */
public class TestProperties implements ITruModuleProperties {

    private static TestProperties INSTANCE=null;

    public TestProperties() {}

    public static synchronized TestProperties getInstance() {
        if (INSTANCE==null) {
            return INSTANCE= new TestProperties();
        }
        return INSTANCE;
    }

    @Override
    public String getPartnerId() {
        return "truRating";
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

    public boolean getIncludeReceipt() {
        return true;
    }

    public boolean getIncludeAcknowledgement() {
        return true;
    }

    public String getDeviceType() {
        return "IPP350";
    }

    public int getDeviceLines() {
        return 4;
    }

    public int getDeviceCpl() {
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

    public String getTruServiceIPAddress() {
///        return "tru-sand-service-fis.cloudapp.net";
        return "http://localhost:47851/api/servicemessage";
//        return "tru-sand-service-trurating.cloudapp.net";
//        return "13.95.156.19";
//        return "127.0.0.1";
    }

    public int getTruServiceSocketTimeoutInMilliSeconds() {
        return 500;
    }

    public int getTruServiceSocketPortNumber() {
        return 9999;
    }
}
