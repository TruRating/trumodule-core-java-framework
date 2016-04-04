package com.trurating.prize;

import com.trurating.device.IDevice;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;

/**
 * Created by Paul on 10/03/2016.
 */
public class CheckForPrize {

    public String doCheck(IDevice iDevice, QuestionResponseJAXB questionResponseJAXB, String prizeCode) {
        if (questionResponseJAXB.getLanguages().getLanguage().getDisplayElements().getPrize() != null) {
            prizeCode = questionResponseJAXB.getLanguages().getLanguage().getDisplayElements().getPrize().toString();
            if (prizeCode.length() > 0) {
                //we have a winner
                iDevice.displayMessageWaitForKey("Congratulations! You won a prize, check your receipt... Press Enter.", 30000);
                iDevice.displayMessage(prizeCode);
            }
        }
        return prizeCode;
    }

}
