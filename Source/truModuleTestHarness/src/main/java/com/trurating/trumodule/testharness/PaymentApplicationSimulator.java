package com.trurating.trumodule.testharness;


import com.trurating.service.v200.xml.RequestTransaction;
import org.apache.log4j.Logger;

import com.trurating.ITruModule;
import com.trurating.TruModule;
import com.trurating.device.IDevice;
import com.trurating.payment.TenderType;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.trumodule.testharness.device.TruRatingConsoleDemoDevice;

/**
 * Created by Paul on 01/03/2016.
 */
public class PaymentApplicationSimulator  {

    private final ITruModule truModule;
    private final Logger log = Logger.getLogger(PaymentApplicationSimulator.class);
    private IDevice iDevice;

    public PaymentApplicationSimulator() {
        this.truModule = new TruModule();
        this.truModule.setDevice(getDevice());
    }

    //this is a take payment questionRequest - payment will not yet be taken
	void paymentTrigger(ITruModuleProperties properties, String operator, TenderType tenderType, String product, long cost){
        log.info("Payment application is requesting payment - passing this on to the module");

        truModule.doRating(properties);

    	RequestTransaction transaction = truModule.getCurrentRatingRecord(properties).getTransaction();

        //Operator
//		transaction.(operator);

    	// Tender type
//   		transaction.s(tenderType.toString());

       	// Amount
       	transaction.setAmount(888) ;
    }

    //now take payment - at this point card is inserted, therefore we will know card type
    public void completePayment(ITruModuleProperties properties) {
    	RequestTransaction transaction = truModule.getCurrentRatingRecord(properties).getTransaction();

        // "INSERT CARD"; //etc

    	// Transaction Id
       	transaction.setId("9999999");

       	// Currency
       	transaction.setCurrency((short)826);
       
    	// Transaction result
//   		transaction.setResult(TransactionResult.APPROVED.toString());

    	// Card type
//   		transaction.set("VISA");

        if (truModule.deliverRating(properties))
        	getDevice().displayMessage("Rating delivery succeeded");
        else
        	getDevice().displayMessage("Rating delivery failed");
    }

    public IDevice getDevice() {
        if (iDevice==null) iDevice = new TruRatingConsoleDemoDevice();
        return iDevice;
    }

}
