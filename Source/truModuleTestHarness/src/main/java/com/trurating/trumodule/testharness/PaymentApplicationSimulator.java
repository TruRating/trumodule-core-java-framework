package com.trurating.trumodule.testharness;


import com.trurating.service.v200.xml.RequestTransaction;
import com.trurating.trumodule.testharness.configuration.TestProperties;
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
    private ITruModuleProperties truModuleProperties;

    public PaymentApplicationSimulator() {
        truModuleProperties = new TestProperties(); // Set of test properties
        this.truModule = new TruModule(truModuleProperties);
        this.truModule.setDevice(getDevice());
    }

    //this is a take payment questionRequest - payment will not yet be taken
	void paymentTrigger(String operator, TenderType tenderType, String product, long cost){
        log.info("Payment application is requesting payment - passing this on to the module");

        truModule.doRating();

    	RequestTransaction transaction = truModule.getCurrentRatingRecord().getTransaction();

        //Operator
//		transaction.(operator);

    	// Tender type
//   		transaction.s(tenderType.toString());

       	// Amount
       	transaction.setAmount(888) ;
    }

    //now take payment - at this point card is inserted, therefore we will know card type
    public void completePayment() {
    	RequestTransaction transaction = truModule.getCurrentRatingRecord().getTransaction();

        // "INSERT CARD"; //etc

    	// Transaction Id
       	transaction.setId("9999999");

       	// Currency
       	transaction.setCurrency((short)826);
       
    	// Transaction result
//   		transaction.setResult(TransactionResult.APPROVED.toString());

    	// Card type
//   		transaction.set("VISA");

        if (truModule.deliverRating())
        	getDevice().displayMessage("Rating delivery succeeded");
        else
        	getDevice().displayMessage("Rating delivery failed");
    }

    public IDevice getDevice() {
        if (iDevice==null) iDevice = new TruRatingConsoleDemoDevice();
        return iDevice;
    }

}
