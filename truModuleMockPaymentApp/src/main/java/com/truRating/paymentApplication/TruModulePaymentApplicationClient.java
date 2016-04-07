package com.trurating.paymentApplication;

import java.io.File;
import java.util.Enumeration;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.trurating.configuration.TestProperties;
import com.trurating.payment.TenderType;

/**
 * Created by Paul on 01/03/2016.
 */
public class TruModulePaymentApplicationClient {

    Logger log = Logger.getLogger(TruModulePaymentApplicationClient.class);

    public static void main(String[] args) {
        new TruModulePaymentApplicationClient().run();
    }

    private void run() {

    	if (!log4JIsConfigured()) {
            configureLog4JUsingDefaults();
        }

    	// Set of test properties
    	TestProperties truModuleProperties = new TestProperties() ;

        //start up the payment App and send a payment request
    	TruModuleMockPaymentApplication paymentApplication = new TruModuleMockPaymentApplication();

        paymentApplication.paymentTrigger(truModuleProperties, "Operator_Tony", TenderType.SMARTCARD, "A Product", 199);

        paymentApplication.completePayment(truModuleProperties);
    }
    

    private void configureLog4JUsingDefaults() {
        FileAppender fileAppender = new FileAppender(); //create appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        File f = new File("defaultTruModule.log");
        fileAppender.setFile("defaultTruModule.log");
        fileAppender.setAppend(true);
        fileAppender.setLayout(new PatternLayout(PATTERN));
        fileAppender.setThreshold(Level.INFO);
        fileAppender.activateOptions();
        Logger.getRootLogger().addAppender(fileAppender);
        String msg = ("\nThis program is using a default log file for logging at: " + f.getAbsolutePath() + "." +
                "\nTo avoid seeing this message, please correctly configure this option from the command line.\n");
        System.out.println(msg);
        log.warn(msg);
    }

    private static boolean log4JIsConfigured() {
        Enumeration appenders = Logger.getRootLogger().getAllAppenders();
        if (appenders.hasMoreElements()) {
            return true;
        } else {
            Enumeration loggers = LogManager.getCurrentLoggers();
            while (loggers.hasMoreElements()) {
                Logger c = (Logger) loggers.nextElement();
                if (c.getAllAppenders().hasMoreElements())
                    return true;
            }
        }
        return false;
    }
}
