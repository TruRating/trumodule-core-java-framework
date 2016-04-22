/*
 * @(#)PrizeManager.java
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

package com.truRating.prize;

//import com.trurating.device.IDevice;
//import com.trurating.xml.LanguageManager;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;

/**
 * Created by Paul on 10/03/2016.
 */
public class PrizeManager {

	private LanguageManager languageManager = null;
	
	private LanguageManager getLanguageManager() {
		if (languageManager == null) 
			languageManager = new LanguageManager() ;
		return languageManager ;
	}
	
	/**
	 * 
	 * @param iDevice
	 * @param questionResponseJAXB
	 * @param prizeCode
	 * @return
	 */
    public String checkForAPrize(IDevice iDevice, QuestionResponseJAXB questionResponseJAXB, String languageCode) {
    	String prizeCode = "";
    	QuestionResponseJAXB.Languages.Language language = getLanguageManager().getLanguage(questionResponseJAXB, languageCode) ;
    	QuestionResponseJAXB.Languages.Language.DisplayElements.Prize prize = language.getDisplayElements().getPrize() ; 

    	if (prize != null) {
            prizeCode = prize.toString();
            if (prizeCode.length() > 0) {
                //we have a winner
                iDevice.displayMessageWaitForKey("Congratulations! You won a prize, check your receipt... Press Enter.", 30000);
                iDevice.displayMessage(prizeCode);
            }
        }
        return prizeCode;
    }

}
