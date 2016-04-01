package com.truRating.paymentApplication;

import com.truRating.moduleSpecific.TruModPaymentRequest;
import com.truRating.truModule.ITruModule;
import com.truRating.truModule.TruModule;
import com.truRating.truSharedData.payment.IPaymentApplication;
import com.truRating.truModule.payment.IPaymentRequest;
import com.truRating.truModule.payment.TenderType;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

/**
 * Created by Paul on 01/03/2016.
 */
public class TruModulePaymentApplicationClient {

    Logger log = Logger.getLogger(TruModulePaymentApplicationClient.class);

    public static void main(String[] args) {
        new TruModulePaymentApplicationClient().run();
    }

    private void run() {

        //start up the ITruModule
        ITruModule truModule = new TruModule();

//        if (ProgramProperties.getInstance().getProperties() ==null) {
//            ProgramProperties.getInstance().initialisePropertiesFromSystemArg();
//            truModule.setProgramProperties(ProgramProperties.getInstance().getProperties());
//        }

        truModule.start();

        //start up the payment App and send a payment request
        IPaymentApplication paymentApplication = new TruModuleMockPaymentApplication(truModule);

        //give the ITruModule a reference to its payment app
//        truModule.setPaymentAppReference(paymentApplication);

//        IPaymentRequest paymentRequest = new TruModPaymentRequest("A Product", new BigDecimal(1.99));
//        paymentRequest.setOperatorId("Operator_Tony");
//        ITenderType tenderType= new TenderType(TenderType.CARD);
//        paymentRequest.setTenderType(tenderType);
//
//        paymentApplication.paymentTrigger(paymentRequest);
//
//        paymentApplication.completePayment(paymentRequest);
    }
}
