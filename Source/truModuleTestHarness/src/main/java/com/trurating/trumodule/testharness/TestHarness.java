package com.trurating.trumodule.testharness;

import java.util.Enumeration;

import org.apache.log4j.*;

import com.trurating.payment.TenderType;
import com.trurating.trumodule.testharness.configuration.TestProperties;

/**
 * Created by Paul on 01/03/2016.
 */
@SuppressWarnings("Duplicates")
public class TestHarness {

    Logger log = Logger.getLogger(TestHarness.class);

    public static void main(String[] args) {
        new TestHarness().run();
    }

    private void run() {

        if (!log4JIsConfigured()) configureLog4JUsingDefaults();

        // start up the payment App and send a payment questionRequest
        PaymentApplicationSimulator paymentApplication = new PaymentApplicationSimulator();
        paymentApplication.paymentTrigger();
        paymentApplication.completePayment();
    }

    private void configureLog4JUsingDefaults() {
        ConsoleAppender fileAppender = new ConsoleAppender(); // create appender
        String PATTERN = "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n";
        fileAppender.setLayout(new PatternLayout(PATTERN));
        fileAppender.setThreshold(Level.INFO);
        fileAppender.activateOptions();
        Logger.getRootLogger().addAppender(fileAppender);
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
