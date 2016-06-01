/*
 * @(#)IXMLNetworkMessenger.java
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

package com.trurating.network.xml;

import com.trurating.properties.ITruModuleProperties;
import com.trurating.service.v200.xml.Request;
import com.trurating.service.v200.xml.Response;

public interface IXMLNetworkMessenger {

    /**
    Send the questionRequest for a question, if no questionResponse after timeOut seconds, will return null;
     */
	Response getResponseFromService(Request request, ITruModuleProperties properties);
	
    void close();
}