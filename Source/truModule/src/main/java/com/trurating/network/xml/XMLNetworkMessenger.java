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
import java.util.concurrent.CountDownLatch;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.trurating.service.v200.xml.*;
import org.apache.log4j.Logger;

import com.trurating.network.ServerConnectionManager;
import com.trurating.properties.ITruModuleProperties;

/**
 * Created by Paul on 08/03/2016.
 */
public class XMLNetworkMessenger implements IXMLNetworkMessenger {

    final private Logger log = Logger.getLogger(XMLNetworkMessenger.class);
	
    /**
     * Socket connections last for the duration of a single transaction
     *  - ie they are used for 2 separate exchanges:
     *  	1 get a question (makes the connection)
     *  	2 deliver a rating (closes the connection)
     */
    private ServerConnectionManager serverConnectionManager;
    
    private volatile Request request = null;
    private volatile Response response = null;

    private Marshaller requestMarshaller;
    private Marshaller responseMarshaller;
    private Unmarshaller responseUnmarshaller;
    private TruRatingMessageFactory truRatingMessageFactory;
    private XMLOutputFactory xmlOutputFactory = null ;

    public XMLNetworkMessenger() {

        try {
            serverConnectionManager = new ServerConnectionManager();
        	
            xmlOutputFactory = XMLOutputFactory.newInstance();

            JAXBContext contextRequest = JAXBContext.newInstance(Request.class);
            JAXBContext contextResponse = JAXBContext.newInstance(Response.class);

            requestMarshaller = contextRequest.createMarshaller();
            requestMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            requestMarshaller.setProperty("jaxb.encoding",  "US-ASCII");
            responseUnmarshaller = contextResponse.createUnmarshaller();

            responseMarshaller = contextResponse.createMarshaller();
            requestMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            requestMarshaller.setProperty("jaxb.encoding",  "US-ASCII");

            truRatingMessageFactory = new TruRatingMessageFactory();

        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
        Will send the questionRequest for a question, if no questionResponse after timeOut seconds, will return null;
     */
        public synchronized Response getResponseFromService(Request request, ITruModuleProperties properties) {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        serverConnectionManager.connectToServer(properties);

        //start listening for a question questionResponse for 'timeout' seconds
        new Thread(new Runnable() {
            public void run() {
                try {
                    log.info("Blocking on question return");
                    final byte[] ba = serverConnectionManager.readInput();
                    final InputStream myInputStream = new ByteArrayInputStream(ba); //this will block until timeout
                    response = (Response) responseUnmarshaller.unmarshal(myInputStream);
                    countDownLatch.countDown();
                    writeResponseToLog(response);
                    return;
                } catch (JAXBException e) {
                    log.error("",e);
                }
                response = null;
                countDownLatch.countDown();
            }
        }).start();

        try {
            //send question questionRequest
            XMLStreamWriter requestWriter = xmlOutputFactory.createXMLStreamWriter(serverConnectionManager.getOutputStream(),
                    (String) requestMarshaller.getProperty(Marshaller.JAXB_ENCODING));
            
            requestWriter.writeStartDocument((String) requestMarshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");
            requestMarshaller.marshal(request, requestWriter);
            requestWriter.writeEndDocument();
            requestWriter.flush();
            writeRequestToLog(request);

        } catch (XMLStreamException e) {
            log.error("",e );
        } catch (JAXBException e) {
            log.error("",e);
        }

        //wait for an answer or a timeout
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("", e);
        }

        return response;
    }

    private void writeResponseToLog(Response response) throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        responseMarshaller.marshal(this.response, baos);
        log.info("truModule [IN]bound message:\n " + new String(baos.toByteArray()));
    }

    private void writeRequestToLog(Request request) throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        requestMarshaller.marshal( request, baos);
        log.info("truModule [IN]bound message:\n " + new String(baos.toByteArray()));
    }


    public void close() {
    	if (serverConnectionManager != null)
            serverConnectionManager.close();
    }
}
