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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;

import com.trurating.network.ServerConnectionManager;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.xml.questionRequest.QuestionRequestJAXB;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;
import com.trurating.xml.ratingResponse.RatingResponseJAXB;

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
    
    private volatile QuestionResponseJAXB questionFromService = null;
    private volatile RatingResponseJAXB ratingResponseJAXB = null;

    private Marshaller questionMarshaller;
    private Unmarshaller questionUnmarshaller;
    private Marshaller ratingMarshaller;
    private Unmarshaller ratingUnmarshaller;
    private TruRatingMessageFactory truRatingMessageFactory;
    private XMLOutputFactory xmlOutputFactory = null ; 

    public XMLNetworkMessenger() {

        try {
            serverConnectionManager = new ServerConnectionManager();
        	
            xmlOutputFactory = XMLOutputFactory.newInstance();

            JAXBContext contextQuestionRequest = JAXBContext.newInstance(QuestionRequestJAXB.class);
            JAXBContext contextQuestionResponse = JAXBContext.newInstance(QuestionResponseJAXB.class);
            JAXBContext contextRatingsDelivery = JAXBContext.newInstance(RatingDeliveryJAXB.class);
            JAXBContext contextRatingsResponse = JAXBContext.newInstance(RatingResponseJAXB.class);

            questionMarshaller = contextQuestionRequest.createMarshaller();
            questionMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            questionMarshaller.setProperty("jaxb.encoding",  "US-ASCII");
            questionUnmarshaller = contextQuestionResponse.createUnmarshaller();

            ratingMarshaller = contextRatingsDelivery.createMarshaller();
            ratingMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            ratingMarshaller.setProperty("jaxb.encoding",  "US-ASCII");
            ratingUnmarshaller = contextRatingsResponse.createUnmarshaller();

            truRatingMessageFactory = new TruRatingMessageFactory();

        } catch (Exception e) {
            log.error(e);
        }
    }
   
    /**
        Will send the request for a question, if no response after timeOut seconds, will return null;
     */
    public synchronized QuestionResponseJAXB getQuestionFromService(ITruModuleProperties properties, long transactionId) {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        serverConnectionManager.connectToServer(properties);

        //start listening for a question response for 'timeout' seconds
        new Thread(new Runnable() {
            public void run() {
                try {
                    log.info("Blocking on question return");
                    final byte[] ba = serverConnectionManager.readInput();
                    final InputStream myInputStream = new ByteArrayInputStream(ba); //this will block until timeout
                    questionFromService = (QuestionResponseJAXB) questionUnmarshaller.unmarshal(myInputStream);
                    countDownLatch.countDown();
                    return;
                } catch (JAXBException e) {
                    log.error(e);
                }
                questionFromService = null;
                countDownLatch.countDown();
            }
        }).start();

        try {
            //send question request
            log.info("Writing questionRequest XML to Service...");
            XMLStreamWriter questionWriter = xmlOutputFactory.createXMLStreamWriter(serverConnectionManager.getOutputStream(),
                    (String) questionMarshaller.getProperty(Marshaller.JAXB_ENCODING));
            
            questionWriter.writeStartDocument((String) questionMarshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");

            //output stream....
            questionMarshaller.marshal(truRatingMessageFactory.assembleARequestQuestion(properties, transactionId), questionWriter);
            questionWriter.writeEndDocument();
            questionWriter.flush();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //wait for an answer or a timeout
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error(e);
        }

        return questionFromService;
    }

	public RatingResponseJAXB deliverRatingToService(RatingDeliveryJAXB ratingRecord) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        //start listening for a rating response for 'timeout' seconds
        new Thread(new Runnable() {
            public void run() {
                try {
                    final byte[] ba = serverConnectionManager.readInput();
                    final InputStream myInputStream = new ByteArrayInputStream(ba); //this will block until timeout
                    ratingResponseJAXB = (RatingResponseJAXB) ratingUnmarshaller.unmarshal(myInputStream);
                    countDownLatch.countDown();
                    log.info("Response received for ratingDelivery");
                    return;
                } catch (JAXBException e) {
                    log.error(e);
                }
                ratingResponseJAXB=null;
                countDownLatch.countDown();
            }
        }).start();

        try {
            //send rating request
            log.info("Writing ratingDelivery XML to Service...");
            XMLStreamWriter ratingWriter = xmlOutputFactory.createXMLStreamWriter(serverConnectionManager.getOutputStream(),
                    (String) ratingMarshaller.getProperty(Marshaller.JAXB_ENCODING));

            ratingWriter.writeStartDocument((String) ratingMarshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");
            ratingMarshaller.marshal( ratingRecord, ratingWriter);
            ratingWriter.writeEndDocument();
            ratingWriter.flush();

        } catch (XMLStreamException e) {
            log.error("Error delivering the rating: ", e);
        } catch (JAXBException e) {
            log.error(e);
        } catch (NumberFormatException e) {
            log.error("There was an error in the ratings delivery factory : " +
                    "perhaps a props file in not correctly set? " + e);
            return null;
        }

        //wait for an answer or a timeout
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error(e);
        }

        if (ratingResponseJAXB.getErrortext()!=null){
            log.info("The truService reported an error with ratings delivery: " + ratingResponseJAXB.getErrortext());
        }

        serverConnectionManager.close();
        return ratingResponseJAXB;
    }
	
    public void close() {
    	if (serverConnectionManager != null)
            serverConnectionManager.close();
    }
}
