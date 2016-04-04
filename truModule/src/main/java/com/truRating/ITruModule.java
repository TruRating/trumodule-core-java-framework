package com.trurating;

import com.trurating.payment.IPaymentResponse;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.rating.Rating;
import com.trurating.device.IDevice;
import com.trurating.payment.IPaymentRequest;

/**
 * Created by Paul on 01/03/2016.
 */
public interface ITruModule {
    
	
	int start();
    
    //if a device ref is set inside the module, the module will use this otherwise it will expect to be able to contact device via payment app.
    void setDeviceRef(IDevice deviceRef);
    
    Rating doRating(ITruModuleProperties properties); //for dwell time where there is no payment request yet
    
    Rating doRating(IPaymentRequest paymentRequest, ITruModuleProperties properties);
    
    
    void cancelRating();
    
    boolean recordRatingResponse(IPaymentResponse paymentResponse, Rating rating, ITruModuleProperties properties);

    /**a method used to setProgramProperties the truModule properties from the payment application,
    /the truModule will default to using its own properties file if one
    isn't setProgramProperties up via this method **/
    void setProgramProperties(ITruModuleProperties propertiesMap);
}
