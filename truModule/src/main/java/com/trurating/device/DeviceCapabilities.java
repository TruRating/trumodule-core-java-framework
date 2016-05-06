/*
 * @(#)DeviceCapabilities.java
 *
 * Copyright (c) 2016 trurating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * trurating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with trurating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT 
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.trurating.device;

import com.trurating.properties.ITruModuleProperties;

/**
 * Created by sasha on 01/03/2016.
 */
public class DeviceCapabilities {

    private int displayRows = 4;
    private int displayColumns = 20;
    private int displayMargin = 0;
    private String deviceType = "IPP350";


    public DeviceCapabilities() {

    }

    public DeviceCapabilities(String deviceType, int displayRows, int displayColumns, int displayMargin) {
        this.displayRows = displayRows;
        this.displayColumns = displayColumns;
        this.displayMargin = displayMargin;
        this.deviceType = deviceType;
    }

    public DeviceCapabilities(ITruModuleProperties properties) {
        this.displayRows = properties.getDeviceLines();
        this.displayColumns = properties.getDeviceCpl();
        this.deviceType = properties.getDeviceType();
        
        // Display margin not currently available through properties
        this.displayMargin = 0;        
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
