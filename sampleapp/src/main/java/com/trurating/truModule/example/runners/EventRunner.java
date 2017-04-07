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
package com.trurating.truModule.example.runners;

import com.trurating.service.v220.xml.*;
import com.trurating.truModule.example.idevice.ConsoleDevice;
import com.trurating.truModule.example.idevice.ConsoleReceiptManager;
import com.trurating.trumodule.TruModuleIntegrated;
import com.trurating.trumodule.device.IDevice;
import com.trurating.trumodule.device.IReceiptManager;
import com.trurating.trumodule.messages.PosParams;
import com.trurating.trumodule.properties.TruModuleProperties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Random;

public class EventRunner implements IRunner {
    private String terminalId;
    private String merchantId;
    private String partnerId;
    private String transportKey;
    private String language;
    private String endpoint;
    private boolean activate;

    private int currentOrderAmount;

    private LinkedList<RequestPosItem> itemCatalog;

    public EventRunner(String terminalId, String merchantId, String partnerId, String transportKey, String language, String endpoint, boolean activate){
        this.terminalId = terminalId;
        this.merchantId = merchantId;
        this.partnerId = partnerId;
        this.transportKey = transportKey;
        this.language = language;
        this.endpoint = endpoint;
        this.activate = activate;
        this.setupItems();
    }
    @Override
    public void run() throws MalformedURLException {

        URL endpointURL = new URL(this.endpoint);
        TruModuleProperties properties = new TruModuleProperties(this.partnerId,this.merchantId,this.terminalId,this.transportKey,endpointURL,this.language);

        // Set the iDevice language from properties.xml
        IDevice consoleDevice = new ConsoleDevice(this.language);
        IReceiptManager receiptManager = new ConsoleReceiptManager();

        // Initialize TruModule
        System.out.println("EVENT RUNNER: Loading TruModuleIntegrated");
        TruModuleIntegrated truModuleIntegrated = new TruModuleIntegrated(properties,receiptManager,consoleDevice);

        // Run activate sequence
        if(this.activate){
            truModuleIntegrated.activate();
        }

        PosParams posParams = new PosParams();
        posParams.setSessionId("CONSOLE_SESSION_" + new Date().getTime());

        currentOrderAmount = 0;
        LinkedList<RequestPosItem> basket = new LinkedList<RequestPosItem>();

        // Send StartTransaction
        System.out.println("INTEGRATED RUNNER: Sending \"Start Transaction\" event");
        truModuleIntegrated.sendPosEvent(posParams,generatePosEvent(POSEvent.REQUESTPOSSTARTTRANSACTION));

        // Send StartTilling
        System.out.println("INTEGRATED RUNNER: Sending \"Start Tilling\" event");
        truModuleIntegrated.sendPosEvent(posParams,generatePosEvent(POSEvent.REQUESTPOSSTARTTILLING));

        // Send Item (n times)
        Random rand = new Random();
        rand.setSeed(System.nanoTime());
        if(rand.nextBoolean()){
            boolean sendItemsInstantly = rand.nextBoolean();
            for (int i = 0; i < rand.nextInt(6); i++){
                if(sendItemsInstantly){
                    System.out.println("INTEGRATED RUNNER: Sending \"Item\" event");
                    truModuleIntegrated.sendPosEvent(posParams,generatePosEvent(POSEvent.REQUESTPOSITEM));
                }
                else{
                    RequestPosItem requestPosItem = chooseRandomItem();
                    this.currentOrderAmount += requestPosItem.getSellingAmount();
                    basket.add(requestPosItem);
                }
            }
            if(!sendItemsInstantly){
                System.out.println("INTEGRATED RUNNER: Sending \"Item (list)\" event");
                RequestPosEvent requestPosEvent = new RequestPosEvent();
                requestPosEvent.getItemOrDiscount().addAll(basket);
                truModuleIntegrated.sendPosEvent(posParams,requestPosEvent);
            }
        }
        else{
            currentOrderAmount = rand.nextInt(100000);
        }

        // Send EndTilling
        System.out.println("INTEGRATED RUNNER: Sending \"End Tilling\" event");
        truModuleIntegrated.sendPosEvent(posParams,generatePosEvent(POSEvent.REQUESTPOSENDTILLING));

        // A short sleep to allow POS events to deliver (only needed during simulation)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.err.println("ERROR: Sleeping thread failed");
        }

        // Initiate payment
        System.out.println("INTEGRATED RUNNER: Initiate payment");
        truModuleIntegrated.initiatePayment(posParams);

        // Send EndTransaction
        System.out.println("INTEGRATED RUNNER: Sending \"End Transaction\" event");
        truModuleIntegrated.sendPosEvent(posParams,generatePosEvent(POSEvent.REQUESTPOSENDTRANSACTION));

        // A short sleep to allow POS events to deliver (only needed during simulation)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.err.println("ERROR: Sleeping thread failed");
        }

        // Send transaction
        System.out.println("INTEGRATED RUNNER: Sending transaction data");
        truModuleIntegrated.sendTransaction(posParams,generateTransactionData(posParams.getSessionId(),currentOrderAmount));
    }
    // Build a transaction object and populate with example data
    private RequestTransaction generateTransactionData(String sessionId,int amount){
        RequestTransaction transaction = new RequestTransaction();

        transaction.setId(sessionId);
        try {
            transaction.setDateTime(getDateTimeNow());
        } catch (DatatypeConfigurationException e) {
            System.err.println("ERROR: Setting date time on transaction failed");
        }
        transaction.setCurrency((short)826);
        transaction.setAmount(amount);
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

    private RequestPosEvent generatePosEvent(POSEvent eventType){
        RequestPosEvent requestPosEvent = new RequestPosEvent();
        switch(eventType){
            case REQUESTPOSSTARTTRANSACTION:
                requestPosEvent.setStartTransaction(new RequestPosStartTransaction());
                break;
            case REQUESTPOSSTARTTILLING:
                requestPosEvent.setStartTilling(new RequestPosStartTilling());
                break;
            case REQUESTPOSITEM:
                RequestPosItem requestPosItem = chooseRandomItem();
                this.currentOrderAmount += requestPosItem.getSellingAmount();
                requestPosEvent.getItemOrDiscount().add(requestPosItem);
                break;
            case REQUESTPOSENDTILLING:
                RequestPosEndTilling requestPosEndTilling = new RequestPosEndTilling();
                requestPosEndTilling.setSubTotalAmount(this.currentOrderAmount);
                requestPosEvent.setEndTilling(requestPosEndTilling);
                break;
            case REQUESTPOSENDTRANSACTION:
                requestPosEvent.setEndTransaction(new RequestPosEndTransaction());
                break;
            default:
                System.err.println("Error! unknown RequestPosEvent type");
        }
        return requestPosEvent;
    }

    private RequestPosItem chooseRandomItem(){
        Random rand = new Random();
        rand.setSeed(System.nanoTime());
        return this.itemCatalog.get(rand.nextInt(this.itemCatalog.size()));
    }

    private void setupItems(){
        this.itemCatalog = new LinkedList<RequestPosItem>();
        RequestPosItem item = new RequestPosItem();
        item.setDescription("Washing powder");
        item.setQuantity(BigDecimal.ONE);
        item.setRetailAmount(150);
        item.setSellingAmount(150);
        item.setUnitAmount(150);
        item.setSku("GDSLANDWP101");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.UNSPECIFIED);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Tomato Soup Tin");
        item.setQuantity(new BigDecimal(3.0));
        item.setRetailAmount(597);
        item.setSellingAmount(597);
        item.setUnitAmount(199);
        item.setSku("GDSFODTINSP24");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.UNSPECIFIED);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Potatoes");
        item.setQuantity(new BigDecimal(5.6));
        item.setRetailAmount(448);
        item.setSellingAmount(448);
        item.setUnitAmount(80);
        item.setSku("GDSFODVEG768");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.KG);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Carrots");
        item.setQuantity(new BigDecimal(2.7));
        item.setRetailAmount(162);
        item.setSellingAmount(162);
        item.setUnitAmount(60);
        item.setSku("GDSFODVEG986");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.UNSPECIFIED);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Cookie Dough Cupcake");
        item.setQuantity(new BigDecimal(4));
        item.setRetailAmount(75);
        item.setSellingAmount(75);
        item.setSku("GDSFODBAK894");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.UNSPECIFIED);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Body Wash");
        item.setQuantity(BigDecimal.ONE);
        item.setRetailAmount(140);
        item.setSellingAmount(140);
        item.setUnitAmount(140);
        item.setSku("GDSHLTBEU274");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.UNSPECIFIED);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Long Life AA Battery");
        item.setQuantity(BigDecimal.TEN);
        item.setRetailAmount(630);
        item.setSellingAmount(630);
        item.setUnitAmount(63);
        item.setSku("GDSELCCNS076");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.UNSPECIFIED);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Pure Orange Juice 1L");
        item.setQuantity(new BigDecimal(2));
        item.setRetailAmount(196);
        item.setSellingAmount(196);
        item.setUnitAmount(98);
        item.setSku("GDSFODDRK670");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.UNSPECIFIED);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Beef Brisket");
        item.setQuantity(new BigDecimal(1.71));
        item.setRetailAmount(1096);
        item.setSellingAmount(1096);
        item.setUnitAmount(641);
        item.setSku("GDSFODBCH188");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.KG);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Baby Ultra Dry Nappies 36 Pack");
        item.setQuantity(BigDecimal.ONE);
        item.setRetailAmount(3750);
        item.setSellingAmount(3750);
        item.setUnitAmount(3750);
        item.setSku("GDSBBYNPY214");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.UNSPECIFIED);
        this.itemCatalog.add(item);
        item = new RequestPosItem();
        item.setDescription("Retractable Ball Pens Black With Grip 10 Pack");
        item.setQuantity(new BigDecimal(4));
        item.setRetailAmount(600);
        item.setSellingAmount(600);
        item.setUnitAmount(150);
        item.setSku("GDSHOMSTA465");
        item.setOperation(Operation.ADD);
        item.setUnitMeasurement(UnitMeasurement.UNSPECIFIED);
        this.itemCatalog.add(item);
    }
}
