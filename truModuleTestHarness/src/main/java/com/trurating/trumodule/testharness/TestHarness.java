package com.trurating.trumodule.testharness;

import java.io.File;
import java.util.Enumeration;

import org.apache.log4j.*;

import com.trurating.payment.TenderType;
import com.trurating.trumodule.testharness.configuration.TestProperties;

/**
 * Created by Paul on 01/03/2016.
 */
public class TestHarness {

	Logger log = Logger.getLogger(TestHarness.class);

	public static void main(String[] args) {
		new TestHarness().run();
	}

	private void run() {

		if (!log4JIsConfigured()) {
			configureLog4JUsingDefaults();
		}

		// Set of test properties
		TestProperties truModuleProperties = new TestProperties();

		// start up the payment App and send a payment request
		PaymentApplicationSimulator paymentApplication = new PaymentApplicationSimulator();

		paymentApplication.paymentTrigger(truModuleProperties, "Operator_Tony",
				TenderType.SMARTCARD, "A Product", 199);

		paymentApplication.completePayment(truModuleProperties);
	}

	private void configureLog4JUsingDefaults() {
		ConsoleAppender fileAppender = new ConsoleAppender(); // create appender
		String PATTERN = "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n";
		File f = new File("defaultTruModule.log");
//		fileAppender.setFile("defaultTruModule.log");
//		fileAppender.setAppend(true);
		fileAppender.setLayout(new PatternLayout(PATTERN));
		fileAppender.setThreshold(Level.INFO);
		fileAppender.activateOptions();
		Logger.getRootLogger().addAppender(fileAppender);
//		String msg = ("\nThis program is using a default log file for logging at: "
//				+ f.getAbsolutePath() + "." + "\nTo avoid seeing this message, please correctly configure this option from the command line.\n");
//		System.out.println(msg);
//		log.warn(msg);
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