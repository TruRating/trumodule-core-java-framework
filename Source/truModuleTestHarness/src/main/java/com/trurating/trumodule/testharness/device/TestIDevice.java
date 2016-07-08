package com.trurating.trumodule.testharness.device;

import com.trurating.device.IDevice;

/**
 * Created by Paul Russell on 27/06/2016.
 */
public interface TestIDevice extends IDevice {

    String getSerialNo();
    boolean isCardIn();
    boolean isCardIsIn();
    void setCardIsIn(boolean cardIsIn);
    String getDisplayMessageText();
    void disableAllInputs();
}
