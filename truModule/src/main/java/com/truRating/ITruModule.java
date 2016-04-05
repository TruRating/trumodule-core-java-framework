package com.trurating;

import com.trurating.device.IDevice;
import com.trurating.payment.IPaymentRequest;
import com.trurating.payment.IPaymentResponse;
import com.trurating.payment.TransactionContext;
import com.trurating.properties.ITruModuleProperties;

/**
 * Created by Paul on 01/03/2016.
 */
public interface ITruModule {
	
	/**
	 *  if a device ref is set inside the module, the module will use this 
	 *  otherwise it will expect to be able to contact device via payment app.
	 * @param deviceRef
	 */
    //
    void setDevice(IDevice deviceRef);
    
	/**
	 * Get a reference to the current transaction data 
	 */
	
	TransactionContext getTransactionContext() ;
	
    /**
     * 
     * @param paymentRequest
     * @param properties
     */
    void doRating(ITruModuleProperties properties, IPaymentRequest paymentRequest);
    
    /**
     * 
     * @param properties
     */
    void doRatingInBackground(ITruModuleProperties properties, String transactionId); //for dwell time where there is no payment request yet

    /**
     * 
     */
    void cancelRating();
    
    /**
     * 
     * @param paymentResponse
     * @param rating
     * @param properties
     * @return
     */
    boolean recordRatingResponse(ITruModuleProperties properties, IPaymentResponse paymentResponse);
}
