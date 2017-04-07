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

package com.trurating.truModule.example.runners;

import com.trurating.service.v220.xml.RequestTransaction;
import com.trurating.service.v220.xml.TransactionResult;
import com.trurating.truModule.example.idevice.ConsoleDevice;
import com.trurating.truModule.example.idevice.ConsoleReceiptManager;
import com.trurating.trumodule.TruModuleStandalone;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.device.IReceiptManager;
import com.trurating.trumodule.properties.TruModuleProperties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;

public class StandaloneRunner implements IRunner{
    private String terminalId;
    private String merchantId;
    private String partnerId;
    private String transportKey;
    private String language;
    private String endpoint;
    private boolean activate;

    public StandaloneRunner(String terminalId, String merchantId, String partnerId, String transportKey, String language, String endpoint, boolean activate){
        this.terminalId = terminalId;
        this.merchantId = merchantId;
        this.partnerId = partnerId;
        this.transportKey = transportKey;
        this.language = language;
        this.endpoint = endpoint;
        this.activate = activate;
    }

    @Override
    public void run() throws MalformedURLException {

        URL endpointURL = new URL(this.endpoint);
        TruModuleProperties properties = new TruModuleProperties(this.partnerId,this.merchantId,this.terminalId,this.transportKey,endpointURL,this.language);

        // Set the iDevice language from properties.xml
        IDevice consoleDevice = new ConsoleDevice(this.language);
        IReceiptManager receiptManager = new ConsoleReceiptManager();

        // Initialize TruModule
        System.out.println("STANDALONE RUNNER: Loading TruModuleStandalone");
        TruModuleStandalone truModuleStandalone = new TruModuleStandalone(properties, receiptManager, consoleDevice);

        // Run activate sequence
        if(this.activate){
            truModuleStandalone.activate();
        }

        // Fetch a question and display it on the iDevice along with a prompt for user input (1AQ1KR).
        // Once the user has provided a response the result is sent to TruService.
        System.out.println("STANDALONE RUNNER: Calling doRating()");
        truModuleStandalone.doRating();

        // A short sleep to allow the response xml to be displayed (only needed in this simulated example)
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.err.println("ERROR: Sleeping thread failed");
        }

        // Generate an example transaction
        RequestTransaction transaction = generateTransactionData();

        // Send transaction to TruService
        System.out.println("STANDALONE RUNNER: Calling sendTransaction()");
        truModuleStandalone.sendTransaction(transaction);
    }

    // Build a transaction object and populate with example data
    private RequestTransaction generateTransactionData(){
        RequestTransaction transaction = new RequestTransaction();

        transaction.setId(Long.toHexString(new Date().getTime()));
        try {
            transaction.setDateTime(getDateTimeNow());
        } catch (DatatypeConfigurationException e) {
            System.err.println("ERROR: Setting date time on transaction failed");
        }
        transaction.setCurrency((short)826);
        transaction.setAmount(5 + (int)(Math.random() * 10000));
        transaction.setResult(TransactionResult.APPROVED);
        transaction.setType("Payment");

        return transaction;
    }

    // Date now in format ready for XML
    private XMLGregorianCalendar getDateTimeNow() throws DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }
}
