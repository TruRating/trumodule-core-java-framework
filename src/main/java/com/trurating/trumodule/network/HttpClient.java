
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

import com.trurating.service.v230.xml.Request;
import com.trurating.service.v230.xml.Response;
import com.trurating.trumodule.properties.ITruModuleProperties;
import com.trurating.trumodule.util.TLSSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The type Http client.
 */
@SuppressWarnings("Duplicates")
public class HttpClient {

    final private Logger logger = LoggerFactory.getLogger(HttpClient.class);


    private TruServiceHttpClient truServiceHttpClient;

    private ITruModuleProperties properties;

    /**
     * Instantiates a new Http client.
     *
     * @param properties the properties
     */
    public HttpClient(ITruModuleProperties properties) {
        logger.debug("Loading HttpClient...");
        this.properties = properties;
        this.truServiceHttpClient = new TruServiceHttpClient(properties);
    }

    /**
     * Instantiates a new Http client.
     *
     * @param properties the properties
     * @param marshaller the marshaller
     */
    public HttpClient(ITruModuleProperties properties, ISerializer marshaller) {
        logger.debug("Loading HttpClient...");
        this.properties = properties;
        this.truServiceHttpClient = new TruServiceHttpClient(properties,marshaller);
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
            int httpTimeout = this.properties.getSocketTimeoutInMilliSeconds();
            connection.setConnectTimeout(httpTimeout);
            // Set read timeout to times 2 connect. So fail fast of we can't connect but allow a little time to read
            connection.setReadTimeout(httpTimeout * 2);
            connection.setChunkedStreamingMode(0);

            connection = truServiceHttpClient.addHeaderInformation(connection, request);

            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)connection).setSSLSocketFactory(new TLSSocketFactory());
            }

            connection.connect();
        } catch (Exception e) {
            logger.error("Error opening connection to {}",url,e);
            return null;
        }

        Response response = sendRequestToHost(connection, request);
        connection.disconnect();
        return response;
    }


    private Response sendRequestToHost(HttpURLConnection urlConnection, Request request) {
        StringWriter sw;
        InputStream inputStream;
        try {
            sw = truServiceHttpClient.serializeRequest(request);
        } catch (Exception e) {
            logger.error("Error serializing request",e);
            return null;
        }

        OutputStream outputStream;
        try {
            outputStream = urlConnection.getOutputStream();
            outputStream.write(sw.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            if(logger.isTraceEnabled()){
                String headers = "";
                String ls = System.getProperty("line.separator");
                Map<String, List<String>> hdrs = urlConnection.getHeaderFields();
                Set<String> hdrKeys = hdrs.keySet();

                for (String k : hdrKeys){
                    headers = headers.concat(k + ": " +  hdrs.get(k) + ls);
                }
                logger.trace("Request: {} {}",headers,sw);
            }

            inputStream = urlConnection.getInputStream();

            if (inputStream == null) {
                logger.error("URLConnection.getInputStream() returns null");
                return null;
            }

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.error("HttpURLConnection.getResponseCode() is {} not 200", urlConnection.getResponseCode());
                return null;
            }

        } catch (IOException e) {
            logger.error("Error sending message to {}",urlConnection.getURL(),e);
            return null;
        }

        String raw = convertStreamToString(inputStream);

        if(logger.isTraceEnabled()){
            String headers = "";
            String ls = System.getProperty("line.separator");
            @SuppressWarnings("SpellCheckingInspection") Map<String, List<String>> hdrs = urlConnection.getHeaderFields();
            Set<String> hdrKeys = hdrs.keySet();

            for (String k : hdrKeys){
                headers = headers.concat(k + ": " +  hdrs.get(k) + ls);
            }
            logger.trace("Response: {} {}",headers,raw);
        }

        Response response;
        try {
            response = truServiceHttpClient.unSerializeResponse(raw);
        } catch (Exception e) {
            logger.error("Error unserializing response",e);
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
