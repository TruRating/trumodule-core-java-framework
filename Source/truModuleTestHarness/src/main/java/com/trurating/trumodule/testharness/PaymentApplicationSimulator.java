package com.trurating.trumodule.testharness;


import com.trurating.properties.ITruModuleProperties;
import com.trurating.service.v200.xml.*;
import com.trurating.trumodule.testharness.configuration.TestProperties;
import com.trurating.trumodule.testharness.device.TestIDevice;
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
    private TestIDevice iDevice;
    private ITruModuleProperties ITruModuleProperties;

    public PaymentApplicationSimulator() {
        ITruModuleProperties = new TestProperties(); // Set of test properties
        this.truModule = new TruModule(ITruModuleProperties);
        this.truModule.setDevice(getDevice());
    }

	void paymentTrigger(){
        log.info("Payment application trigger activated - passing this on to the module");
        truModule.doRating();
    }

    public void completePayment() {
        if (truModule.getCachedTruModuleRatingObject()!=null) { //if it does equal null there was no question to ask, and so now rating to deliver
            truModule.getCachedTruModuleRatingObject().transaction = returnNewTransaction(); //imagine this was done using a real ped...

            try {
                String response;
                if (truModule.deliverRating() == TruModule.RATING_DELIVERY_OUTCOME_SUCCEEDED) {
                    response = truModule.getCachedTruModuleRatingObject().responseWithRating;
                } else {
                    response = truModule.getCachedTruModuleRatingObject().receiptNoRating;
                }
                getDevice().displayMessage(response);
            } catch (Exception e) {
                log.error(e);
            }

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            truModule.cancelRating();
            truModule.clearAllCachedModuleData();
        }

        iDevice.disableAllInputs();
    }

    private RequestTransaction returnNewTransaction() {
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
        return transaction;
    }

    public IDevice getDevice() {
        if (iDevice==null) iDevice = new TruRatingConsoleDemoDevice();
        return iDevice;
    }

}
