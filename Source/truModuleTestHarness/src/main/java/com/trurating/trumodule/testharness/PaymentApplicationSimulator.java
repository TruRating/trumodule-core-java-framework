package com.trurating.trumodule.testharness;


import com.trurating.properties.ITruModuleProperties;
import com.trurating.service.v200.xml.*;
import com.trurating.trumodule.testharness.configuration.TestProperties;
import org.apache.log4j.Logger;

import com.trurating.ITruModule;
import com.trurating.TruModule;
import com.trurating.device.IDevice;
import com.trurating.payment.TenderType;
import com.trurating.trumodule.testharness.device.TruRatingConsoleDemoDevice;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Paul on 01/03/2016.
 */
public class PaymentApplicationSimulator  {

    private final ITruModule truModule;
    private final Logger log = Logger.getLogger(PaymentApplicationSimulator.class);
    private IDevice iDevice;
    private ITruModuleProperties ITruModuleProperties;

    public PaymentApplicationSimulator() {
        ITruModuleProperties = new TestProperties(); // Set of test properties
        this.truModule = new TruModule(ITruModuleProperties);
        this.truModule.setDevice(getDevice());
    }

    //this is a take payment questionRequest - payment will not yet be taken
	void paymentTrigger(String operator, TenderType tenderType, String product, long cost){
        log.info("Payment application is requesting payment - passing this on to the module");
        truModule.doRating();
    }

    public void completePayment() {

        truModule.getCurrentRatingRecord().getTransaction();
        RequestTransaction transaction = new RequestTransaction();
       	transaction.setId("9999999");
       	transaction.setCurrency((short)826);
        transaction.setAmount(550);
   		transaction.setResult(TransactionResult.APPROVED);

        RequestTender requestTender = new RequestTender();
        requestTender.setAmount(550);
        RequestCardHash requestCardHash = new RequestCardHash();
        requestCardHash.setType(CardHashType.CDH_1);
        requestCardHash.setValue("67687546785643876587436");
        requestTender.setCardHash(requestCardHash);
        requestTender.setTenderType(com.trurating.service.v200.xml.TenderType.CREDIT);
        transaction.getTender().add(requestTender);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        transaction.setDateTime(sdf.format(new Date()));
        if (truModule.deliverRating(transaction))
        	getDevice().displayMessage("Rating delivery succeeded");
        else
        	getDevice().displayMessage("Rating delivery failed");
    }

    public IDevice getDevice() {
        if (iDevice==null) iDevice = new TruRatingConsoleDemoDevice();
        return iDevice;
    }

}
