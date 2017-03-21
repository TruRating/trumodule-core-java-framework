
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
     * Sets partner id.
     *
     * @param partnerId the partner id
     */
    void setPartnerId(String partnerId);

    /**
     * Gets merchant id.
     *
     * @return the merchant id
     */
    String getMerchantId();

    /**
     * Sets merchant id.
     *
     * @param merchantId the merchant id
     */
    @SuppressWarnings("unused")
    void setMerchantId(String merchantId);

    /**
     * Gets terminal id.
     *
     * @return the terminal id
     */
    String getTerminalId();

    /**
     * Sets terminal id.
     *
     * @param terminalId the terminal id
     */
    @SuppressWarnings("unused")
    void setTerminalId(String terminalId);

    /**
     * Gets transport key.
     *
     * @return the transport key
     */
    String getTransportKey();

    /**
     * Sets transport key.
     *
     * @param transportKey the transport key
     */
    void setTransportKey(String transportKey);

    /**
     * Gets socket timeout in milli seconds.
     *
     * @return the socket timeout in milli seconds
     */
    int getSocketTimeoutInMilliSeconds();

    /**
     * Sets socket timeout in milli seconds.
     *
     * @param milliSeconds the milli seconds
     */
    void setSocketTimeoutInMilliSeconds(int milliSeconds);

    /**
     * Gets tru service url.
     *
     * @return the tru service url
     */
    URL getTruServiceURL();

    /**
     * Sets tru service url.
     *
     * @param url the url
     */
    void setTruServiceURL(URL url);

    /**
     * Gets rfc.
     *
     * @return the rfc
     */
    String getRFC();

    /**
     * Sets rfc.
     *
     * @param rfc the rfc
     */
    @SuppressWarnings("unused")
    void setRFC(String rfc);
}
