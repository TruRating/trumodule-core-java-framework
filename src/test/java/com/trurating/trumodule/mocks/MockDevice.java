/*
 *
 *  The MIT License
 *
 *  Copyright (c) 2017 TruRating Ltd. https://www.trurating.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *
 */
package com.trurating.trumodule.mocks;

import com.trurating.service.v230.xml.*;
import com.trurating.trumodule.device.IDevice;

public class MockDevice implements IDevice {
    @Override
    public void displayMessage(String string) {
        System.out.println("IDEVICE: " + string);
    }

    @Override
    public void displayMessage(String string, int timeout) {
        System.out.println("IDEVICE: " + string);
    }

    @Override
    public int display1AQ1KR(String promptText, int questionTimeout) {
        return 4;
    }

    @Override
    public void resetDisplay() {
        System.out.println("---RESET DISPLAY---");
    }

    @Override
    public String readLine(String promptText) {
        if(promptText.contains("number")){
            return "4";
        }
        if(promptText.contains("registration code")){
            return "";
        }
        return "UNITTEST_READLINE_RESPONSE";
    }

    @Override
    public RequestPeripheral getScreenCapabilities() {
        RequestPeripheral requestPeripheral = new RequestPeripheral();
        requestPeripheral.setFont(Font.MONOSPACED);
        requestPeripheral.setFormat(Format.RAW);
        requestPeripheral.setHeight((short)10);
        requestPeripheral.setWidth((short)20);
        requestPeripheral.setSeparator("|");
        requestPeripheral.setUnit(UnitDimension.LINE);
        return requestPeripheral;
    }

    @Override
    public SkipInstruction getSkipInstruction() {
        return SkipInstruction.CLEARLOWER;
    }

    @Override
    public String getCurrentLanguage() {
        return "en-GB";
    }

    @Override
    public RequestLanguage[] getLanguages() {
        RequestLanguage[] requestLanguages = new RequestLanguage[2];
        requestLanguages[0] = new RequestLanguage();
        requestLanguages[0].setRfc1766("en-GB");
        requestLanguages[1] = new RequestLanguage();
        requestLanguages[1].setRfc1766("fr-CA");
        return requestLanguages;
    }

    @Override
    public RequestServer getServer() {
        RequestServer requestServer = new RequestServer();
        requestServer.setFirmware("UNITTEST_V1");
        requestServer.setId("UNITTEST_1");
        return requestServer;
    }

    @Override
    public String getName() {
        return "UNITTEST_NAME_1";
    }

    @Override
    public String getFirmware() {
        return "UNITTEST_V1";
    }
}
