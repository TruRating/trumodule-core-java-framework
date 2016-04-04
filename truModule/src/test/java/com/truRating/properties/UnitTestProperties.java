package com.trurating.properties;

import com.trurating.properties.ITruModuleProperties;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Paul on 2016. :)
 */
public class UnitTestProperties implements ITruModuleProperties {

    private static Logger log = Logger.getLogger(UnitTestProperties.class);
    private Properties properties = null;
    private File propertiesFile;
    private String pathToResources;
    private static UnitTestProperties INSTANCE=null;

    private UnitTestProperties() {}

    public static synchronized UnitTestProperties getInstance() {
        if (INSTANCE==null) {
            return INSTANCE= new UnitTestProperties();
        }
        return INSTANCE;
    }

    public String getMid() {
        return "123456";
    }

    public String getTid() {
        return "65321";
    }

    public String getLanguageCode() {
        return "en";
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
        return "RAW";
    }

    public String getDeviceFirmware() {
        return "RAM0973";
    }

    public String getDeviceFontType() {
        return "FIXED";
    }

    public String getServerId() {
        return "1";
    }

    public String getPpaFirmware() {
        return "MCM4.2";
    }

    public String getTruServiceIPAddress() {
        return "40.76.5.14";
    }

    public int getTruServiceSocketTimeoutInMilliSeconds() {
        return 500;
    }

    public int getTruServiceSocketPortNumber() {
        return 9999;
    }
}
