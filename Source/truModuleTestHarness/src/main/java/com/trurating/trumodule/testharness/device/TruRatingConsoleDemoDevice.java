package com.trurating.trumodule.testharness.device;

import com.trurating.device.DeviceCapabilities;
import com.trurating.device.IDevice;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Paul on 07/03/2016.
 */
public class TruRatingConsoleDemoDevice implements IDevice {

    Logger log = Logger.getLogger(TruRatingConsoleDemoDevice.class);
    private JFrame jFrame = null;

    public TruRatingConsoleDemoDevice() {
        jFrame = new JFrame();
        jFrame.setTitle("TruRating Console Mockup");
        int height = 200;
        int width = 200;
        jFrame.setSize(200, 200);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation(dim.width/2-width/2, dim.height/2-height/2);
        jFrame.setVisible(true);
        jFrame.requestFocus();
    }

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
        JOptionPane.showMessageDialog(jFrame, string);
        log.info(string);
    }

    public void displayPleaseWait() {
    	String string = "Please wait..." ;
        JOptionPane.showMessageDialog(jFrame, string);
        log.info(string);
    }

    public void displayMessage(String string) {
        JOptionPane.showMessageDialog(jFrame, string);
    }

    public void displayMessageWaitForKey(String string, int timeout) {
        log.info("Displaying message and waiting for key");
        JOptionPane.showConfirmDialog(
                jFrame,
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
        Object[] ratingValues = {1,2,3,4,5,6,7,8,9,0};
        Integer integer = (Integer)JOptionPane.showInputDialog(
                jFrame,
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
        return integer.toString();
    }

    public void cancelInput() {
        jFrame.setVisible(false);
        return;
    }

}
