/*
 * @(#)XMLNetworkMessenger.java
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

package com.trurating.network.xml;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.trurating.properties.ITruModuleProperties;
import com.trurating.service.v200.xml.*;
import org.apache.log4j.Logger;

/**
 * Created by Paul on 08/03/2016.
 */
public class XMLNetworkMessenger implements IXMLNetworkMessenger {

    final private Logger log = Logger.getLogger(XMLNetworkMessenger.class);

    private Marshaller requestMarshaller;
    private Marshaller responseMarshaller;
    private Unmarshaller responseUnmarshaller;
    private XMLOutputFactory xmlOutputFactory = null;
    private URL url;
    private int httpTimeout;


    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setConnectTimeout(httpTimeout);
        connection.setRequestProperty("Accept", "application/xml");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/xml");
        return connection;
    }

    public XMLNetworkMessenger(ITruModuleProperties ITruModuleProperties) {

        try {
            url = new URL(ITruModuleProperties.getTruServiceURL());
            httpTimeout = ITruModuleProperties.getSocketTimeoutInMilliSeconds();

            log.info("**** TRUXMLNETWORKMESSENGER SETUP ****");

            xmlOutputFactory = XMLOutputFactory.newInstance();

            JAXBContext contextRequest = JAXBContext.newInstance(Request.class);
            JAXBContext contextResponse = JAXBContext.newInstance(Response.class);

            requestMarshaller = contextRequest.createMarshaller();
            requestMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            responseUnmarshaller = contextResponse.createUnmarshaller();

            responseMarshaller = contextResponse.createMarshaller();
            requestMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        } catch (Exception e) {
            log.error("", e);
        }
    }

    public synchronized Response getResponseRatingFromRatingsDeliveryToService(Request request) {
        return exchangeMessageWithServer(request);
    }

    public synchronized Response getResponseQuestionFromService(Request request) {
        return exchangeMessageWithServer(request);
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private Response exchangeMessageWithServer(Request request) {
        Response response = null;

        try {

            HttpURLConnection httpURLConnection;

            httpURLConnection = getConnection(url);
            URLConnection urlConnection = httpURLConnection;
            OutputStream outputStream = null;
            try {
                outputStream = urlConnection.getOutputStream();
            } catch (java.net.ConnectException e) {
                log.error("Unable to contact the service!");
                return null;
            }
            XMLStreamWriter requestWriter = xmlOutputFactory.createXMLStreamWriter(outputStream,
                    (String) requestMarshaller.getProperty(Marshaller.JAXB_ENCODING));

            requestWriter.writeStartDocument((String) requestMarshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");
            requestMarshaller.marshal(request, requestWriter);
            requestWriter.writeEndDocument();
            requestWriter.flush();
            requestWriter.close();
            writeRequestToLog(request);

            InputStream inputStream = urlConnection.getInputStream();
            String raw = convertStreamToString(inputStream);
            log.info("[IN][RAW]: " + raw);

            inputStream = new ByteArrayInputStream(raw.getBytes());
            response = (Response) responseUnmarshaller.unmarshal(inputStream);

            if (httpURLConnection.getResponseCode() > 200) {
                log.error("There was a problem with the http exchange, please see server logs for more details");
                return null;
            }

            writeResponseToLog(response);

        } catch (XMLStreamException e) {
            log.error("", e);
        } catch (JAXBException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }

        return response;
    }

    private void writeResponseToLog(Response response) throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        responseMarshaller.marshal(response, baos);
        log.info("truModule [IN]bound [POST]marshalling message:\n " + new String(baos.toByteArray()));
    }

    private void writeRequestToLog(Request request) throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        requestMarshaller.marshal(request, baos);
        log.info("truModule [OUT]bound [POST}marshalling message:\n " + new String(baos.toByteArray()));
    }
}
