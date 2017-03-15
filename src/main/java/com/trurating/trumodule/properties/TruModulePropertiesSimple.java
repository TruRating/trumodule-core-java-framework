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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The type Tru module properties simple.
 */
@SuppressWarnings("unused")
public class TruModulePropertiesSimple implements ITruModuleProperties{
    private String partnerId;
    private String merchantId;
    private String terminalId;
    private String transportKey;
    private int socketTimeout;
    private URL truServiceURL;
    private String rfc;

    /**
     * Instantiates a new Tru module properties simple.
     *
     * @param partnerId  the partner id
     * @param merchantId the merchant id
     * @param terminalId the terminal id
     * @throws MalformedURLException the malformed url exception
     */
    public TruModulePropertiesSimple(String partnerId, String merchantId, String terminalId) throws MalformedURLException{
        this( partnerId, merchantId, terminalId, "nulltransportkey12345678", new URL("http://tru-sand-service-v200.trurating.com/api"), "en-GB", 500);
    }

    /**
     * Instantiates a new Tru module properties simple.
     *
     * @param partnerId     the partner id
     * @param merchantId    the merchant id
     * @param terminalId    the terminal id
     * @param transportKey  the transport key
     * @param truServiceURL the tru service url
     */
    public TruModulePropertiesSimple(String partnerId, String merchantId, String terminalId, String transportKey, URL truServiceURL){
        this( partnerId, merchantId, terminalId, transportKey, truServiceURL, "en-GB", 500);
    }

    /**
     * Instantiates a new Tru module properties simple.
     *
     * @param partnerId     the partner id
     * @param merchantId    the merchant id
     * @param terminalId    the terminal id
     * @param transportKey  the transport key
     * @param truServiceURL the tru service url
     * @param rfc           the rfc
     */
    public TruModulePropertiesSimple(String partnerId, String merchantId, String terminalId, String transportKey, URL truServiceURL, String rfc){
        this( partnerId, merchantId, terminalId, transportKey, truServiceURL, rfc, 500);
    }

    /**
     * Instantiates a new Tru module properties simple.
     *
     * @param partnerId     the partner id
     * @param merchantId    the merchant id
     * @param terminalId    the terminal id
     * @param transportKey  the transport key
     * @param truServiceURL the tru service url
     * @param rfc           the rfc
     * @param socketTimeout the socket timeout
     */
    @SuppressWarnings("WeakerAccess")
    public TruModulePropertiesSimple(String partnerId, String merchantId, String terminalId, String transportKey, URL truServiceURL, String rfc, int socketTimeout){
        this.partnerId = partnerId;
        this.merchantId = merchantId;
        this.terminalId = terminalId;
        this.transportKey = transportKey;
        this.truServiceURL = truServiceURL;
        this.rfc = rfc;
        this.socketTimeout = socketTimeout;
    }

    @Override
    public String getPartnerId() {
        return this.partnerId;
    }

    @Override
    public String getMerchantId() {
        return this.merchantId;
    }

    @Override
    public String getTerminalId() {
        return this.terminalId;
    }

    @Override
    public String getTransportKey() {
        return this.transportKey;
    }

    @Override
    public int getSocketTimeoutInMilliSeconds() {
        return this.socketTimeout;
    }

    @Override
    public URL getTruServiceURL() {
        return this.truServiceURL;
    }

    @Override
    public String getRFC() {
        return this.rfc;
    }
}
