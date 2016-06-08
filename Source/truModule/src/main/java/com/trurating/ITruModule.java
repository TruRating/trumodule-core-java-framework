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
import com.trurating.service.v200.xml.RequestRating;
import com.trurating.service.v200.xml.RequestTransaction;

/**
 * Created by Paul on 01/03/2016.
 */
public interface ITruModule {

    void setDevice(IDevice deviceRef);

    RequestRating getCurrentRatingRecord(); // return the current ratings record

    void doRating(); //display the question on the ped, and take a user keystroke

    void doRatingInBackground(); //display the question on the ped, and take a user keystroke - run in background thread

    void cancelRating(); //can away all ped activity - used when a payment arrives and ped needs to be clear

    boolean deliverRating(RequestTransaction transaction); //deliver the rating the the truService

    String getReceiptMessage(); // for use in receipt printing

    void close();

    void clearCachedRatingInformation();

    void createNewTransaction();

    void updateTransaction(RequestTransaction requestTransaction);

    RequestTransaction getCurrentTransaction();
}
