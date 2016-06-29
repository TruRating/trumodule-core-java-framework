package com.trurating.trumodule.testharness.device;

import com.trurating.TruModule;
import com.trurating.device.DeviceCapabilities;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

/**
 * Created by Paul on 07/03/2016.
 */
@SuppressWarnings("Duplicates")
public class TruRatingConsoleDemoDevice implements TestIDevice {

    private Logger log = Logger.getLogger(TruRatingConsoleDemoDevice.class);
    private JFrame jFrame = null;
    private JPanel interiorPanel = new JPanel();
    private JLabel displayTextLabel;
    private JComboBox ratingsBox;
    private JButton cancelButton;
    private JButton proceedButton;
    private volatile int ratingVal = 0;
    private boolean cardIsIn = false;
    private String welcomeText = "Welcome to Trurating PED";
    private volatile boolean paymentWentThroughOkay = false;

    public static void main(String[] args) {
        new TruRatingConsoleDemoDevice();
    }

    public TruRatingConsoleDemoDevice() {
        jFrame = new JFrame();
        jFrame.setTitle("TruRating Console");
        int height = 300;
        int width = 1000;
        jFrame.setSize(width, height);
        interiorPanel.setSize(width, height);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
        jFrame.setVisible(true);
        displayTextLabel = new JLabel("Please rate!!!!");
        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();
        for (int i = 0; i <= 9; i++) {
            defaultComboBoxModel.addElement(Integer.toString(i));
        }
        ratingsBox = new JComboBox();
        ratingsBox.setModel(defaultComboBoxModel);
        interiorPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 50, 100));
        interiorPanel.add(displayTextLabel);
        interiorPanel.add(ratingsBox);
        interiorPanel.setVisible(true);
        cancelButton = new JButton("Cancel");
        proceedButton = new JButton("Proceed");
        interiorPanel.add(cancelButton);
        proceedButton.setVisible(false);
        interiorPanel.add(proceedButton);
        jFrame.add(interiorPanel);
        jFrame.requestFocus();
        cardIsIn = false;
    }

    @Override
    public String getSerialNo() {
        return "1234576";
    }

    public DeviceCapabilities getDeviceCapabilities() {
        DeviceCapabilities deviceCapabilities = new DeviceCapabilities();
        deviceCapabilities.setDeviceType("TruRating Device");
        deviceCapabilities.setDisplayColumns(16);
        deviceCapabilities.setDisplayMargin(0);
        deviceCapabilities.setDisplayRows(4);
        return deviceCapabilities;
    }


    public boolean isCardIn() {
        return cardIsIn;
    }

    public boolean isCardIsIn() {
        return cardIsIn;
    }

    public void setCardIsIn(boolean cardIsIn) {
        this.cardIsIn = cardIsIn;
    }

    @Override
    public String getDisplayMessageText() {
        return displayTextLabel.getText();
    }

    @Override
    public void disableAllInputs() {
        cancelButton.setEnabled(false);
        ratingsBox.setEnabled(false);
        displayTextLabel.setText("The test cycle is finished.");
    }

    public int getKey(int timeout) {
        log.info("Displaying message and waiting for key");
        ConsoleManager consoleManager = new ConsoleManager();
        return new Integer(consoleManager.getKey());
    }

    public void displayWelcome() {
        displayTextLabel.setText(welcomeText);
    }

    public void displayPleaseWait() {
        displayTextLabel.setText("Please wait...");
    }


    public void displayMessage(String string) {
        displayTextLabel.setText(string);
    }

    public void displayMessageWaitForKey(String string, final int timeout) {
        log.error("Not implemented");
    }

    public String displayTruratingQuestionGetKeystroke(String[] prompts, String promptText, final int timeout) {

        displayTextLabel.setText(promptText);
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ratingVal = TruModule.NO_RATING_VALUE;
                log.info("User cancelled rating: " + ratingVal);
                ratingsBox.setVisible(false);
                cancelButton.setVisible(false);
                countDownLatch.countDown();
            }
        });

        ratingsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ratingVal = ratingsBox.getSelectedIndex();
                log.info("User rated: " + ratingVal);
                ratingsBox.setVisible(false);
                cancelButton.setVisible(false);
                countDownLatch.countDown();
            }
        });

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }
            }
        });

        try {
            while (!future.isDone()) Thread.sleep(10);
        } catch (InterruptedException e) {
        }
        executorService.shutdown();
        ActionListener[] al = cancelButton.getListeners(ActionListener.class);
        cancelButton.removeActionListener(al[0]);
        al = ratingsBox.getListeners(ActionListener.class);
        ratingsBox.removeActionListener(al[0]);


        return new Integer(ratingVal).toString();
    }

    public void cancelInput() {
        displayTextLabel.setText("Input cancelled");
        return;
    }

    //this is a one off method not in other Idevice to enable us to fake out a take payment on the simulator
    public boolean doPayment(String amount_transaction) {
        displayTextLabel.setText("Taking payment for £"+ amount_transaction);
        cancelButton.setVisible(true);
        proceedButton.setVisible(true);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final long timeout =60000;

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentWentThroughOkay=false;
                log.info("User selected cancel payment");
                countDownLatch.countDown();
            }
        });

        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentWentThroughOkay=true;
                log.info("User selected proceed with payment");
                countDownLatch.countDown();
            }
        });


        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }
            }
        });

        try {
            while (!future.isDone()) Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        executorService.shutdown();
        ActionListener[] al = cancelButton.getListeners(ActionListener.class);
        cancelButton.removeActionListener(al[0]);
        al = proceedButton.getListeners(ActionListener.class);
        proceedButton.removeActionListener(al[0]);

        return paymentWentThroughOkay;
    }
}
