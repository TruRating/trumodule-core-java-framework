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

package com.trurating.truModule.example;

import com.trurating.truModule.example.runners.EventRunner;
import com.trurating.truModule.example.runners.IRunner;
import com.trurating.truModule.example.runners.StandaloneRunner;

import java.io.*;
import java.net.MalformedURLException;

import java.net.UnknownHostException;
import java.util.Properties;

public class Application {
    public static void main(String[] args) {

        // Turn on Trace level logging in TruModule to display the xml exchanges
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");

        // Read simulation properties from resources/properties.xm
        Properties properties = loadProperties("sampleapp/src/main/resources/properties.xml");

        // Display welcome message (also checked that all properties are set)
        displayWelcome(properties);

        if (properties != null && !properties.containsKey("TerminalId")) {
            java.net.InetAddress localMachine;
            String localMachineName;
            try {
                localMachine = java.net.InetAddress.getLocalHost();
                localMachineName = localMachine.getHostName();
            } catch (UnknownHostException e) {
                localMachineName = "ConsoleDevice";
            }
            properties.setProperty("TerminalId", localMachineName);
        }

        IRunner runner;

        boolean activate = (properties != null && (properties.getProperty("AllowRegistration").equalsIgnoreCase("true")));

        // At this time only standalone terminal simulation has been implemented (https://docs.trurating.com/get-started/standalone-terminals/)
        String posIntegration = (properties != null ? properties.getProperty("PosIntegration") : "");
        if (posIntegration.equalsIgnoreCase("None")) {
            //noinspection ConstantConditions
            runner = new StandaloneRunner(properties.getProperty("TerminalId"), properties.getProperty("MerchantId"), properties.getProperty("PartnerId"), properties.getProperty("TransportKey"), properties.getProperty("Languages"), properties.getProperty("Endpoint"), activate);
        } else if (posIntegration.equalsIgnoreCase("Events")) {
            //noinspection ConstantConditions
            runner = new EventRunner(properties.getProperty("TerminalId"), properties.getProperty("MerchantId"), properties.getProperty("PartnerId"), properties.getProperty("TransportKey"), properties.getProperty("Languages"), properties.getProperty("Endpoint"), activate);
        } else {
            System.err.println("ERROR: Value of property \"PosIntegration\" is invalid or unsupported");
            System.exit(1);
            return;
        }

        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                // Display a start message (and wait for user input)
                displayStart();

                // Run the simulation
                runner.run();

                // Put a 1 sec sleep in to allow response xml from server to be printed to stdout before starting again
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println("ERROR: Sleeping thread failed");
                }
            }

        } catch (MalformedURLException e) {
            System.err.println("ERROR: Value of property \"Endpoint\" is invalid");
            System.exit(1);
        }
    }

    private static Properties loadProperties(@SuppressWarnings("SameParameterValue") String fileLocation) {
        try {
            File file = new File(fileLocation);
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.loadFromXML(fileInput);
            fileInput.close();

            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void displayWelcome(Properties properties) {
        System.out.println("TruModule example version: 1.0.0.0");
        System.out.println("====== Settings ======");
        System.out.println("");
        System.out.println("Override any of these settings from the configuration file");
        System.out.println("");

        boolean missingProperty = false;

        if (properties.containsKey("Endpoint")) {
            System.out.println("Endpoint          :" + properties.getProperty("Endpoint"));
        } else {
            System.out.println("Endpoint          :MISSING");
            missingProperty = true;
        }
        if (properties.containsKey("TerminalId")) {
            System.out.println("TerminalId        :" + properties.getProperty("TerminalId"));
        } else {
            java.net.InetAddress localMachine;
            String localMachineName;
            try {
                localMachine = java.net.InetAddress.getLocalHost();
                localMachineName = localMachine.getHostName();
            } catch (UnknownHostException e) {
                localMachineName = "ConsoleDevice";
            }

            System.out.println("TerminalId        :MISSING (using " + localMachineName + ")");
        }
        if (properties.containsKey("MerchantId")) {
            System.out.println("MerchantId        :" + properties.getProperty("MerchantId"));
        } else {
            System.out.println("MerchantId        :MISSING");
            missingProperty = true;
        }
        if (properties.containsKey("PartnerId")) {
            System.out.println("PartnerId         :" + properties.getProperty("PartnerId"));
        } else {
            System.out.println("PartnerId         :MISSING");
            missingProperty = true;
        }
        if (properties.containsKey("TransportKey")) {
            System.out.println("TransportKey      :" + properties.getProperty("TransportKey"));
        } else {
            System.out.println("TransportKey      :MISSING");
            missingProperty = true;
        }
        if (properties.containsKey("Languages")) {
            System.out.println("Languages         :" + properties.getProperty("Languages"));
        } else {
            System.out.println("Languages         :MISSING");
            missingProperty = true;
        }
        if (properties.containsKey("PosIntegration")) {
            System.out.println("PosIntegration    :" + properties.getProperty("PosIntegration"));
        } else {
            System.out.println("PosIntegration    :MISSING");
            missingProperty = true;
        }
        if (properties.containsKey("AllowRegistration")) {
            System.out.println("AllowRegistration :" + properties.getProperty("AllowRegistration"));
        } else {
            System.out.println("AllowRegistration :MISSING");
            missingProperty = true;
        }

        if (missingProperty) {
            System.out.println("");
            System.out.println("ERROR: One or more properties are unset or invalid");
            System.exit(1);
        }
    }

    private static void displayStart() {

        Console c = System.console();
        if (c != null) {
            c.format("\n");
            c.format("====== Ready to start a new simulation... ======\n");
            c.format("Press ENTER to start (or Ctrl+C to exit):\n");
            c.readLine();
        } else {
            System.out.println("");
            System.out.println("====== Ready to start a new simulation... ======");
            System.out.println("Press ENTER to start (or Ctrl+C to exit):");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
