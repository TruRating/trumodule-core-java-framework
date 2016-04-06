package com.trurating;

import com.trurating.device.IDevice;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;

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
	 * Get a reference to the current record  
	 */
	
    RatingDeliveryJAXB getRatingRecord(ITruModuleProperties properties) ;
	
    /**
     * 
     * @param paymentRequest
     * @param properties
     */
    void doRating(ITruModuleProperties properties);
    
    /**
     * Background ratings are used for dwell time to allow a cancel command 
     * to be issued in the foreground thread if a payment request is made
     * @param properties
     */
    void doRatingInBackground(ITruModuleProperties properties); 

    /**
     * Clear any question that is still being displayed on the device, 
     * to allow payment to proceed
     */
    void cancelRating();
       
    /**
     * 
     * @param paymentResponse
     * @param rating
     * @param properties
     * @return
     */
    boolean deliverRating(ITruModuleProperties properties);
    
	/** 
	 * The message that should appear on the receipt as a consequence
	 * of the outcome of this rating question
	 */
	String getReceiptMessage() ;
    
    /**
     * If we are mid transaction, then clean up the open socket.
     */
    void close() ;
}
