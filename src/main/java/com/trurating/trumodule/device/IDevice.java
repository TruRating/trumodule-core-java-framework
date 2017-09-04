
/*
 *  The MIT License
 *
 *  Copyright (c) 2017 TruRating Ltd. https://www.trurating.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.trurating.trumodule.device;

import com.trurating.service.v230.xml.*;

/**
 * An interface that describes the behaviour that the trurating
 * application requires of a PIN entry device.
 */
public interface IDevice {
    /**
     * Display a message on the display of the PIN entry device
     *
     * @param string the string
     */
    void displayMessage(String string);

    /**
     * Display message.
     *
     * @param string  the string
     * @param timeout the timeout
     */
    @SuppressWarnings("unused")
    void displayMessage(String string, int timeout);

    /**
     * Display the TruRating question and wait for a single keystroke. The last parameter is optional
     *
     * @param promptText      the prompt text
     * @param questionTimeout the question timeout
     * @return the keypress that was captured
     */
    int display1AQ1KR(String promptText, int questionTimeout);

    /**
     * Reset the display and cancel and pending commands
     */
    void resetDisplay();

    String readLine(String promptText);

    /**
     * Gets request device.
     *
     * @return the request device
     */
    RequestPeripheral getScreenCapabilities();


    SkipInstruction getSkipInstruction();

    /**
     * Gets rfc 1766 language code.
     *
     * @return the rfc 1766 language code
     */
    String getCurrentLanguage();

    /**
     * Get languages request language [ ].
     *
     * @return the request language [ ]
     */
    @SuppressWarnings("unused")
    RequestLanguage[] getLanguages();

    /**
     * Gets server.
     *
     * @return the server
     */
    RequestServer getServer();

    String getName();

    String getFirmware();
}
