/*
 * @(#)TRGlobalConstants.java
 *
 * Copyright (c) 2013 truRating Limited. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * truRating Limited. ("Confidential Information").  You shall
 * not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with truRating Limited.
 *
 * TRURATING LIMITED MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT 
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TRURATING LIMITED
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT 
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

/*
 * VERSION HISTORY:
 *
 * 1.00 2 Apr 2014 Initial Version
 */

package com.truRating.truSharedData.common;

/**
 * A top level class to hold the ID's that globally apply to this implementation
 * of the trurating specification
 * 
 * @author Peter
 * 
 */
public class TRGlobalConstants {

	// The unique identifier for the group of trurating partners responsible for
	// providing and deploying this implementation of trurating
	public final static String ImplementorsTruratingPartnerID = "300";

	// The version of the trurating specification that this implementation
	// conforms to
	public final static String SpecificationVersion = "v114";

	public static String getTransportKey() {
		return "054645644822251160531737";
	}
}
