package com.truRating.truModule.device;

/**
 * Created by sasha on 01/03/2016.
 */
public class DeviceCapabilities {

    private int displayRows = 4;
    private int displayColumns = 20;
    private int displayMargin = 0;
    private String deviceType = "IPP350";

    public DeviceCapabilities(String deviceType, int displayRows, int displayColumns) {
        this.displayRows = displayRows;
        this.displayColumns = displayColumns;
        this.displayMargin = displayMargin;
        this.deviceType = deviceType;
    }

    public DeviceCapabilities() {

    }

    public int getDisplayRows() {
        return displayRows;
    }

    public void setDisplayRows(int displayRows) {
        this.displayRows = displayRows;
    }

    public int getDisplayColumns() {
        return displayColumns;
    }

    public void setDisplayColumns(int displayColumns) {
        this.displayColumns = displayColumns;
    }

    public int getDisplayMargin() {
        return displayMargin;
    }

    public void setDisplayMargin(int displayMargin) {
        this.displayMargin = displayMargin;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
