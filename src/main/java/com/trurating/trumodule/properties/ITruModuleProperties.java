
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
package com.trurating.trumodule.properties;

import java.net.URL;

/**
 * This interface defines a set of method that TruModule will need to function.
 * In this implementation, these are read from the trumodule.properties file.
 */
public interface ITruModuleProperties {

    /**
     * Gets partner id.
     *
     * @return the partner id
     */
    String getPartnerId();

    /**
     * Gets merchant id.
     *
     * @return the merchant id
     */
    String getMerchantId();

    /**
     * Gets terminal id.
     *
     * @return the terminal id
     */
    String getTerminalId();

    /**
     * Gets transport key.
     *
     * @return the transport key
     */
    String getTransportKey();

    /**
     * Gets socket timeout in milli seconds.
     *
     * @return the socket timeout in milli seconds
     */
    int getSocketTimeoutInMilliSeconds();

    /**
     * Gets tru service url.
     *
     * @return the tru service url
     */
    URL getTruServiceURL();

    /**
     * Gets rfc.
     *
     * @return the rfc
     */
    String getRFC();

}
