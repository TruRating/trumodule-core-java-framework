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
import com.trurating.service.v200.xml.RequestRating;
import com.trurating.service.v200.xml.RequestTransaction;

/**
 * Created by Paul on 01/03/2016.
 */
public interface ITruModule {

    void setDevice(IDevice deviceRef);

    RequestRating getCurrentRatingRecord(ITruModuleProperties properties);

    void doRating(ITruModuleProperties properties);

    void doRatingInBackground(ITruModuleProperties properties);

    void cancelRating();

    boolean deliverRating(ITruModuleProperties properties);

    String getReceiptMessage();

    void close();

    void clearValueOfCachedRatingAndReceipt();
}
