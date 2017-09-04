/*
 *
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
 *
 */
package com.trurating.trumodule.network;

import com.trurating.service.v230.xml.Request;
import com.trurating.service.v230.xml.Response;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.security.MacSignatureCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

/**
 * The type Tru service http client.
 */
class TruServiceHttpClient {
    final private Logger logger = LoggerFactory.getLogger(TruServiceHttpClient.class);

    private ISerializer marshaller;
    private String transportKey = null;

    /**
     * Instantiates a new Http client.
     *
     * @param properties the properties
     */
    TruServiceHttpClient(ITruModuleProperties properties) {
        this(properties, new JAXBSerializer());
    }

    /**
     * Instantiates a new Http client.
     *
     * @param properties the properties
     * @param marshaller the marshaller
     */
    TruServiceHttpClient(ITruModuleProperties properties, ISerializer marshaller) {
        logger.debug("Loading TruServiceHttpClient...");
        this.transportKey = properties.getTransportKey();
        if (marshaller != null) {
            this.marshaller = marshaller;
        }
    }

    /**
     * Serialize request string writer.
     *
     * @param request the request
     * @return the string writer
     */
    StringWriter serializeRequest(Request request) {
        return this.marshaller.marshall(request);
    }

    /**
     * Un serialize response response.
     *
     * @param raw the raw
     * @return the response
     */
    Response unSerializeResponse(String raw) {
        return this.marshaller.unMarshall(raw);
    }

    /**
     * Add header information http url connection.
     *
     * @param connection the connection
     * @param request    the request
     * @return the http url connection
     */
    HttpURLConnection addHeaderInformation(HttpURLConnection connection, Request request) {
        connection.setRequestProperty("x-tru-api-partner-id", request.getPartnerId());
        connection.setRequestProperty("x-tru-api-merchant-id", request.getMerchantId());
        connection.setRequestProperty("x-tru-api-terminal-id", request.getTerminalId());
        connection.setRequestProperty("x-tru-api-encryption-scheme", Integer.toString(MacSignatureCalculator.getApiEncryptionScheme()));

        String data = null;
        try {
            data = marshaller.marshall(request).toString();
        } catch (Exception e) {
            logger.error("Error marshalling request for Maccing", e);
        }
        try {
            byte[] hash = MacSignatureCalculator.calculateMac(data != null ? data.getBytes("UTF-8") : new byte[0], transportKey);
            connection.setRequestProperty("x-tru-api-mac", new String(hash != null ? hash : new byte[0]));
        } catch (UnsupportedEncodingException e) {
            logger.error("Error calculating MAC", e);
        }

        connection.setRequestProperty("Accept", "application/xml");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/xml");
        return connection;
    }
}
