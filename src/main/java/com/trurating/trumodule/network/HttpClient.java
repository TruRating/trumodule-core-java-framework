
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

package com.trurating.trumodule.network;

import com.trurating.service.v210.xml.Request;
import com.trurating.service.v210.xml.Response;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.security.MacSignatureCalculator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * The type Http client.
 */
@SuppressWarnings("Duplicates")
public class HttpClient {

    final private Logger log = Logger.getLogger(HttpClient.class.getName());

    private IMarshaller marshaller;
    private String transportKey = null;

    /**
     * Instantiates a new Http client.
     *
     * @param properties the properties
     */
    public HttpClient(ITruModuleProperties properties){
        this(properties,new JAXBMarshallSerialiser());
    }

    /**
     * Instantiates a new Http client.
     *
     * @param properties the properties
     * @param marshaller the marshaller
     */
    public HttpClient(ITruModuleProperties properties, IMarshaller marshaller) {
        log.info("Loading HttpClient...");
        try {
            this.transportKey = properties.getTransportKey();
            if(marshaller != null){
                this.marshaller = marshaller;
            }
        } catch (Exception e) {
            log.severe("Error starting com.trurating.trumodule.network.HttpClient");
            log.severe(e.toString());
        }
    }

    /**
     * Send response.
     *
     * @param url     the url
     * @param request the request
     * @return the response
     */
    public synchronized Response send(URL url, Request request) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            int httpTimeout = 1000;
            connection.setConnectTimeout(httpTimeout);
            connection.setReadTimeout(httpTimeout);
        } catch (Exception e) {
            log.severe(e.toString());
            return null;
        }

        connection = addHeaderInformation(connection, request);
        Response response = sendRequestToHost(connection, request);
        connection.disconnect();
        return response;
    }

    private HttpURLConnection addHeaderInformation(HttpURLConnection connection, Request request) {
        connection.setRequestProperty("x-tru-api-partner-id", request.getPartnerId());
        connection.setRequestProperty("x-tru-api-merchant-id", request.getMerchantId());
        connection.setRequestProperty("x-tru-api-terminal-id", request.getTerminalId());
        connection.setRequestProperty("x-tru-api-encryption-scheme", Integer.toString(MacSignatureCalculator.getApiEncryptionScheme()));

        String data = null;
        try {
            data = marshaller.marshall(request).toString();
        } catch (Exception e) {
            log.severe(e.toString());
        }
        try {
            byte[] hash = MacSignatureCalculator.calculateMac(data != null ? data.getBytes("UTF-8") : new byte[0], transportKey);
            connection.setRequestProperty("x-tru-api-mac", new String(hash != null ? hash : new byte[0]));
        } catch (UnsupportedEncodingException e) {
            log.severe(e.toString());
        }

        connection.setRequestProperty("Accept", "application/xml");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/xml");
        return connection;
    }

    private Response sendRequestToHost(HttpURLConnection urlConnection, Request request) {
        StringWriter sw;
        InputStream inputStream;
        try {
            sw = marshaller.marshall(request);
        } catch (Exception e) {
            log.severe(e.toString());
            return null;
        }

        OutputStream outputStream;
        try {
            outputStream = urlConnection.getOutputStream();
            outputStream.write(sw.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            inputStream = urlConnection.getInputStream();

            if (inputStream==null) {
                log.severe("URLConnection.getInputStream() returns null");
                return null;
            }

            if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
                log.severe("HttpURLConnection.getResponseCode() is not HTTP_ACCEPTED");
                return null;
            }

        } catch (IOException e) {
            return null;
        }

        String raw = convertStreamToString(inputStream);
        log.info(raw);
        Response response;

        try {
            response = marshaller.unMarshall(raw);
        } catch (Exception e) {
            log.severe(e.toString());
            return null;
        }
        return response;
    }

    /**
     * Convert stream to string string.
     *
     * @param is the is
     * @return the string
     */
    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is);
        s.useDelimiter("\\A");
        String data = s.hasNext() ? s.next() : "";
        s.close();
        return data;
    }
}
