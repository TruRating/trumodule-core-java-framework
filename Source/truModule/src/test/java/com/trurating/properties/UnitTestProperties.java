package com.trurating.properties;

/**
 * Created by Paul on 2016. :)
 */
public class UnitTestProperties implements ITruModuleProperties {

    private static UnitTestProperties INSTANCE=null;

    private UnitTestProperties() {}

    public static synchronized UnitTestProperties getInstance() {
        if (INSTANCE==null) {
            return INSTANCE= new UnitTestProperties();
        }
        return INSTANCE;
    }

    @Override
    public String getPartnerId() {
        return "1";
    }

    public String getMid() {
        return "TRU-150";
    }

    public String getTid() {
        return "PAULRUSS";
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
        return "RAW";
    }

    public String getDeviceFirmware() {
        return "RAM0973";
    }

    public String getDeviceFontType() {
        return "MONOSPACED";
    }

	public int getQuestionTimeout() {
		return 50000;
	}

	public String getServerId() {
        return "1";
    }

    public String getPpaFirmware() {
        return "MCM4.2";
    }

    public String getTruServiceIPAddress() {
        //return "http://localhost:47851/api/servicemessage";
        return "http://tru-sand-service-v200.trurating.com/api/servicemessage";
    }

    public int getTruServiceSocketTimeoutInMilliSeconds() {
        return 500;
    }

    public int getTruServiceSocketPortNumber() {
        return 9999;
    }
}
