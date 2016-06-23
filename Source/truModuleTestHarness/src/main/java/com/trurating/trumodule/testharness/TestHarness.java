package com.trurating.trumodule.testharness;

import org.apache.log4j.*;

/**
 * Created by Paul on 01/03/2016.
 */
public class TestHarness {

    Logger log = Logger.getLogger(TestHarness.class);

    public static void main(String[] args) {
        new TestHarness().run();
    }

    private void run() {

        new SetUpLog4JDelegate().setupLog4J();

        // start up the payment App and send a payment questionRequest
        PaymentApplicationSimulator paymentApplication = new PaymentApplicationSimulator();
        paymentApplication.paymentTrigger();
        paymentApplication.completePayment();
    }



}
