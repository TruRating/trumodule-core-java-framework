package com.trurating.paymentApplication;

import org.apache.log4j.Logger;

import com.trurating.ITruModule;
import com.trurating.device.IDevice;
import com.trurating.device.TruRatingConsoleDemoDevice;
import com.trurating.payment.IPaymentApplication;
import com.trurating.payment.IPaymentRequest;
import com.trurating.payment.IPaymentResponse;
import com.trurating.payment.transaction.ITransactionResult;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.utilTime.TDate;
import com.trurating.utilTime.TTime;

/**
 * Created by Paul on 01/03/2016.
 */
public class TruModuleMockPaymentApplication implements IPaymentApplication {

    private final ITruModule truModule;
    private final Logger log = Logger.getLogger(TruModuleMockPaymentApplication.class);
    private IDevice iDevice;

    public TruModuleMockPaymentApplication(ITruModule truModule) {
        this.truModule = truModule;
    }

    //this is a take payment request - payment will not yet be taken
    public void paymentTrigger(IPaymentRequest paymentRequest, ITruModuleProperties properties) {
        log.info("Payment application is requesting payment - passing this on to the module");
        truModule.doRating(properties, paymentRequest);
    }

    //now take payment - at this point card is inserted, therefore we will know card type
    public void completePayment(IPaymentRequest paymentRequest, ITruModuleProperties properties) {
        IPaymentResponse paymentResponse = new TruModulePaymentResponse(); //again only partially implemented

        //do card
        if (paymentRequest.getTenderType().equals("CARD")) {
            getDevice().displayMessage("INSERT CARD"); //etc

            //for demo purposes
            paymentResponse.setCardScheme("VISA");
            paymentResponse.setOperatorId(paymentRequest.getOperatorId());
            paymentResponse.setTransactionIsApproved();
            log.info("Payment cleared");

        } else
        //do cash
        if (paymentRequest.getTenderType().equals("CASH")) {
        }

        //for demo purposes, assume that payment cleared
        paymentResponse.setTransactionNumber(1234512345);
        paymentResponse.setTransactionDate(new TDate().toString());
        paymentResponse.setTransactionTime(new TTime().toString());
        paymentResponse.setCardHashType("CDH1");
        paymentResponse.setCardHashData("7eb094ef3537a0a8c7799cc8725aa77fc3632ceaa193594492cd422d736fa79e");
        paymentResponse.setTenderType(paymentRequest.getTenderType());

        truModule.recordRatingResponse(properties, paymentResponse);
    }

    public IDevice getDevice() {
        if (iDevice==null) iDevice = new TruRatingConsoleDemoDevice();
        return iDevice;
    }

    public boolean hasTruratingEnabledAndActive() {
        return true;
    }

    public void updateTrurating() {
        log.error("Not implemented yet!");
    }

    public boolean initialise() {
        return false;
    }

    public void startTransaction(String tillOperator, String salesPerson) {
        log.info("Starting transaction");
    }

    public void startTransaction(String tillOperator, String salesPerson, String txnID) {
        log.error("Not implemented yet");
    }

    public void endTransaction(ITransactionResult result) {
        log.info("End transaction");
    }

    public void startCheckout() {
        log.info("Starting checkout");

    }

    public void endCheckout(IPaymentRequest paymentDetails) {
        log.info("Starting end checkout");

    }

//    public void endTransaction(ITransactionResult result) {
//        log.info("Starting end transaction");
//
//    }

    public String getErrorMessage() {
        return null;
    }

    public void shutdown() {
        log.info("Payment application system shutdown called");
        System.exit(1);
    }


    public ITruModule getTruModule() {
        return truModule;
    }
}
