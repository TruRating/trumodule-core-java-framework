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
 */

package com.trurating.truModule.example.idevice;

import com.trurating.service.v220.xml.*;
import com.trurating.truModule.example.idevice.consoleUtils.ConsoleInput;
import com.trurating.trumodule.device.IDevice;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

// An IDevice modeled using the console
public class ConsoleDevice implements IDevice {
    private RequestLanguage[] requestLanguages;
    private String currentLanguage;
    private ConsoleInput con;

    public ConsoleDevice(String languages){
        String[] values = languages.split(",");
        this.requestLanguages = new RequestLanguage[values.length];
        for(int i = 0;i < values.length;i++){
            this.requestLanguages[i] = new RequestLanguage();
            this.requestLanguages[i].setRfc1766(values[i]);
        }
        this.currentLanguage = values[0];
    }

    @Override
    public void displayMessage(String s) {
        System.out.println("IDEVICE: " + s);
    }

    @Override
    public void displayMessage(String s, int i) {
        System.out.println("IDEVICE(timeout:" + i + "): " + s);
    }

    @Override
    public int display1AQ1KR(String s, int i) {
        System.out.println("IDEVICE: " + s);
        this.con = new ConsoleInput(
                i,
                TimeUnit.MILLISECONDS
        );

        int result;
        try {
            String input = this.con.readLine();

            if(input == null){
                return -2;
            }

            if(input.equals("")){
                // User did not enter a rating (user skipped)
                return -1;
            }

            result = Integer.parseInt(input);

            if(result > 9 || result < 0){
                return -4;
            }

        } catch (InterruptedException e) {
            return -2;
        } catch (NumberFormatException e){
            System.err.println("Error parsing input from terminal");
            return -4;
        }
        return result;
    }

    @Override
    public void resetDisplay() {
        System.out.println("IDEVICE: ---RESET DISPLAY---");
        if(con != null){
            con.cancel();
        }
    }

    @Override
    public String readLine(String s) {
        System.out.println("IDEVICE: " + s);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
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
        return SkipInstruction.DEFAULT;
    }

    @Override
    public String getCurrentLanguage() {
        return this.currentLanguage;
    }

    @Override
    public RequestLanguage[] getLanguages() {
        return this.requestLanguages;
    }

    @Override
    public RequestServer getServer() {
        RequestServer requestServer = new RequestServer();
        requestServer.setFirmware("CONSOLE 2.0");
        requestServer.setId("Console Device");
        return requestServer;
    }

    @Override
    public String getName() {
        return "Console Device";
    }

    @Override
    public String getFirmware() {
        return "CONSOLE 2.0";
    }
}
