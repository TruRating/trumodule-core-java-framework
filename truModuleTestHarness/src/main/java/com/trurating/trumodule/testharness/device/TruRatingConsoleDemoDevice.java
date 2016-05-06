package com.trurating.trumodule.testharness.device;

import com.trurating.device.DeviceCapabilities;
import com.trurating.device.IDevice;

import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * Created by Paul on 07/03/2016.
 */
public class TruRatingConsoleDemoDevice implements IDevice {

    Logger log = Logger.getLogger(TruRatingConsoleDemoDevice.class);

    public String getSerialNo() {
        return "1234576";
    }

    public DeviceCapabilities getDeviceCapabilities() {
        return new DeviceCapabilities();
    }

    public boolean isCardIn() {
        return false;
    }

    public int getKey(int timeout) {
        log.info("Displaying message and waiting for key");
        ConsoleManager consoleManager = new ConsoleManager();
        return new Integer(consoleManager.getKey());
    }

    public void displayWelcome() {
    	String string = "trurating welcome" ; 
        JOptionPane.showMessageDialog(null, string);
        log.info(string);
    }

    public void displayPleaseWait() {
    	String string = "Please wait..." ; 
        JOptionPane.showMessageDialog(null, string);
        log.info(string);
    }

    public void displayMessage(String string) {
        JOptionPane.showMessageDialog(null, string);
        log.info(string);
    }

    public void displayMessageWaitForKey(String string, int timeout) {
        log.info("Displaying message and waiting for key");
        JOptionPane.showConfirmDialog(
                null,
                string,
                "Winner!",
                JOptionPane.CLOSED_OPTION);
        return;
    }

    public String displaySecurePromptGetKeystroke(String[] prompts, String promptText, int timeout) {
        log.info("Displaying message and waiting for key");
        ConsoleManager consoleManager = new ConsoleManager();
        return new Integer(consoleManager.getKey()).toString();
    }

    public String displayTruratingQuestionGetKeystroke(String[] prompts, String promptText, int timeout) {
        log.info(promptText);

        Object[] ratingValues = {1,2,3,4,5,6,7,8,9,0};
        Integer integer = (Integer)JOptionPane.showInputDialog(
                null,
                promptText,
                "Please rate",
                JOptionPane.PLAIN_MESSAGE,
                null,
                ratingValues,
                9);

        //If a string was returned, say so.
        if ((integer == null)) {
            integer=-1;
        }

//        ConsoleManager consoleManager = new ConsoleManager();
//        log.info("---> KEYBOARD INPUT REQUIRED!! <---");
//        return new Integer(consoleManager.getKey()).toString();
        return integer.toString();
    }

    public void cancelInput() {
        return;
    }

}
