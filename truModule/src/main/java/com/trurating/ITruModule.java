/*
 * @(#)ITruModule.java
 *
 * Copyright (c) 2016 trurating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * trurating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with trurating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT 
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.trurating;

import com.trurating.device.IDevice;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;

/**
 * Created by Paul on 01/03/2016.
 */
public interface ITruModule {

    /**
     * if a device ref is set inside the module, the module will use this
     * otherwise it will expect to be able to contact device via payment app.
     */
    void setDevice(IDevice deviceRef);

    /**
     * Get a reference to the current record
     */

    RatingDeliveryJAXB.Transaction getCurrentRatingRecordTransaction();

    RatingDeliveryJAXB getCurrentRatingRecord(ITruModuleProperties properties);

    void doRating(ITruModuleProperties properties);

    /**
     * Background ratings are used for dwell time to allow a cancel command
     * to be issued in the foreground thread if a payment questionRequest is made
     */
    void doRatingInBackground(ITruModuleProperties properties);

    /**
     * Clear any question that is still being displayed on the device,
     * to allow payment to proceed
     */
    void cancelRating();

    boolean deliverRating(ITruModuleProperties properties);

    /**
     * The message that should appear on the receipt as a consequence
     * of the outcome of this rating question
     */
    String getReceiptMessage();

    /**
     * If we are mid transaction, then clean up the open socket.
     */
    void close();

    void clearValueOfCachedRatingAndReceipt();
}
