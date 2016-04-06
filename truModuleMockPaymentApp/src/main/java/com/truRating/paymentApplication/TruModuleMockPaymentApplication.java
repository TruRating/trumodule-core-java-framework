package com.trurating.paymentApplication;

import org.apache.log4j.Logger;

import com.trurating.ITruModule;
import com.trurating.TruModule;
import com.trurating.device.IDevice;
import com.trurating.device.TruRatingConsoleDemoDevice;
import com.trurating.payment.IPaymentRequest;
import com.trurating.payment.TenderType;
import com.trurating.payment.TransactionResult;
import com.trurating.payment.transaction.ITransactionResult;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB.Transaction;

/**
 * Created by Paul on 01/03/2016.
 */
public class TruModuleMockPaymentApplication  {

    private final ITruModule truModule;
    private final Logger log = Logger.getLogger(TruModuleMockPaymentApplication.class);
    private IDevice iDevice;

    public TruModuleMockPaymentApplication() {
        this.truModule = new TruModule();
        this.truModule.setDevice(getDevice());
    }

    private Transaction getTransaction(ITruModuleProperties properties) {
    	return truModule.getRatingRecord(properties).getTransaction() ; 
    }

    //this is a take payment request - payment will not yet be taken
	void paymentTrigger(ITruModuleProperties properties, String operator, TenderType tenderType, String product, long cost){
        log.info("Payment application is requesting payment - passing this on to the module");

        truModule.doRating(properties);
        
    	Transaction transaction = getTransaction(properties) ; 

        //Operator
		transaction.setOperator(operator);        
    	
    	// Tender type
   		transaction.setTendertype(tenderType.toString());
        
       	// Amount
       	transaction.setAmount(199) ;
    }

    //now take payment - at this point card is inserted, therefore we will know card type
    public void completePayment(ITruModuleProperties properties) {
    	Transaction transaction = getTransaction(properties) ; 

        getDevice().displayMessage("INSERT CARD"); //etc

    	// Transaction Id
       	transaction.setTxnid(12345);

       	// Currency
       	transaction.setCurrency((short)826);
       
    	// Transaction result
   		transaction.setResult(TransactionResult.APPROVED.toString());

    	// Card type
   		transaction.setCardtype("VISA");

        truModule.deliverRating(properties);
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
