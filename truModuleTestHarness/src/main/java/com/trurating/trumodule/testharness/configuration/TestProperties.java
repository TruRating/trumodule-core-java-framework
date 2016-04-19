package com.trurating.trumodule.testharness.configuration;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.trurating.properties.ITruModuleProperties;

/**
 * Created by Paul on 2016. :)
 */
public class TestProperties implements ITruModuleProperties {

    private static Logger log = Logger.getLogger(TestProperties.class);
    private Properties properties = null;
    private File propertiesFile;
    private String pathToResources;
    private static TestProperties INSTANCE=null;

    public TestProperties() {}

    public static synchronized TestProperties getInstance() {
        if (INSTANCE==null) {
            return INSTANCE= new TestProperties();
        }
        return INSTANCE;
    }

    public String getMid() {
        return "123456";
    }

    public String getTid() {
        return "00000tR2";
        //return "12345";
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
        return "TEXT_CENTER";
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
        return "127.0.0.1";
//        return "13.95.156.19";
    }

    public int getTruServiceSocketTimeoutInMilliSeconds() {
        return 500;
    }

    public int getTruServiceSocketPortNumber() {
        return 9999;
    }
}
