/*
 * @(#)TruModule.java
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

package com.trurating;

import com.trurating.network.xml.IXMLNetworkMessenger;
import org.apache.log4j.Logger;

import com.trurating.device.IDevice;
import com.trurating.network.xml.TruRatingMessageFactory;
import com.trurating.network.xml.XMLNetworkMessenger;
import com.trurating.prize.PrizeManager;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.util.StringUtilities;
import com.trurating.xml.LanguageManager;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.DisplayElements.Question;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB.Rating;
import com.trurating.xml.ratingResponse.RatingResponseJAXB;
import com.trurating.xml.ratingResponse.RatingResponseJAXB.Languages.Language.Receipt;

/**
 * TruModule is the main class of a library that encapsulates the behaviour 
 * required by a payment application to 
 * 	- connect to the truService application
 * 	- retrieve a question, 
 * 	- run a rating question, 
 * 	- check for a prize, 
 * 	- issue an appropriate receipt message
 *  - and deliver the rating to truService 
 * 
 * Created by Paul on 01/03/2016.
 * 
 */
public class TruModule implements ITruModule  {

	public final static short NO_RATING_VALUE = -99 ;
	public final static short USER_CANCELLED = -1 ;
	public final static short QUESTION_TIMEOUT = -2 ;
	public final static short NO_QUESTION_ASKED = -3 ;
	
    private final Logger log = Logger.getLogger(TruModule.class);

    private IDevice iDevice = null;
    private IXMLNetworkMessenger xmlNetworkMessenger = null;
	private TruRatingMessageFactory truRatingMessageFactory = null;
    private final PrizeManager checkForPrize = new PrizeManager();
    private String receiptMessage = "";
	private LanguageManager languageManager = null;
	    
    private volatile RatingDeliveryJAXB currentRatingRecord = null ;
    
    public TruModule() {
		truRatingMessageFactory = new TruRatingMessageFactory() ;
		xmlNetworkMessenger = new XMLNetworkMessenger();
    }

    // Set the device in use
    public void setDevice(IDevice deviceRef) {
        this.iDevice = deviceRef;
    }

    private IDevice getDevice() {
        return iDevice;
    }
    
	// Get a reference to the current transaction data
	public RatingDeliveryJAXB getRatingRecord(ITruModuleProperties properties) {
		if (currentRatingRecord == null)
			currentRatingRecord = truRatingMessageFactory.createRatingRecord(properties) ;
		return currentRatingRecord;
	}

	private LanguageManager getLanguageManager() {
		if (languageManager == null) 
			languageManager = new LanguageManager() ;
		return languageManager ;
	}
	
    /**
     * Request to run a rating question
     * If this needs to be done in a background thread then the assumption is
     * that the thread will already have been created and this call will have been made from it.
     */
    public void doRating(ITruModuleProperties properties) {
    	// Ensure that we are starting a new rating record - nothing left over from before
    	endTransaction() ;
    	
    	// The rating class generates its own unique (for this till) transaction id
    	QuestionResponseJAXB jaxbQuestion = getNextQuestion(properties) ;
    	
    	runQuestion (properties, jaxbQuestion) ;
    }

	/**
	 * 	Dwell time ratings questions need to be run in a separate thread
	 */	
    public void doRatingInBackground(final ITruModuleProperties properties) {

    	// Ensure that we are starting a new rating record - nothing left over from before
    	endTransaction() ;
    	
    	// The rating class generates its own unique (for this till) transaction id
    	final QuestionResponseJAXB jaxbQuestion = getNextQuestion(properties) ;

    	new Thread(new Runnable() 
    			{
    				public void run() { 
    					runQuestion (properties, jaxbQuestion) ;
    				}
    			}
    	).start() ;
    }

    /**
     * Clear the question ready for payment
     */
	public void cancelRating() {
        getDevice().cancelInput();
    }

    public boolean deliverRating(ITruModuleProperties properties) {

    	boolean rv = false ;
        try {
        	if (currentRatingRecord == null) 
        		log.error("Call to deliverRating when no rating record exists!");
        	
        	else {
        		//send the rating
        		final RatingResponseJAXB ratingResponseJAXB = xmlNetworkMessenger.deliverRatingToService(currentRatingRecord);
        		if (ratingResponseJAXB != null) {
        			if ((ratingResponseJAXB.getErrortext() != null) && (ratingResponseJAXB.getErrortext().length() > 0))
        				log.error(ratingResponseJAXB.getErrortext());
        			else { //todo this needs to be on a /language basis
        				final RatingResponseJAXB.Languages.Language language = getLanguageManager().getLanguage(ratingResponseJAXB, properties.getLanguageCode());
        				final Receipt ratingResponseReceipt = language.getReceipt();

        				if (currentRatingRecord.getRating().getValue() > 0)
        					setReceiptMessage(ratingResponseReceipt.getRatedvalue());
        				else
        					setReceiptMessage(ratingResponseReceipt.getNotratedvalue());

        				rv = true ;
        			}
        		}
        	}
        } catch (Exception e) {
            log.error("", e);
        }
        finally {
        	endTransaction() ;
        }        

        log.info("Everything is finished in truModule!");
        return rv;
    }

    private QuestionResponseJAXB getNextQuestion(ITruModuleProperties properties) {
    	QuestionResponseJAXB questionResponseJAXB = null ;

        try {        	
        	RatingDeliveryJAXB ratingRecord = getRatingRecord(properties) ;
        	long transactionId = ratingRecord.getUid().longValue();
            questionResponseJAXB = xmlNetworkMessenger.getQuestionFromService(properties, transactionId);
            
            if (questionResponseJAXB.getErrortext() != null) {
                if (!questionResponseJAXB.getErrortext().equals("")) {
                    log.error("The QuestionRequest produced an error : " + questionResponseJAXB.getErrortext());
                    return null;
                }
            }
            else if (questionResponseJAXB != null) {
            	QuestionResponseJAXB.Languages.Language language = 
            			getLanguageManager().getLanguage(questionResponseJAXB, properties.getLanguageCode()) ;
            	
            	if (language.getDisplayElements().getQuestion().getValue().length() > 0) {
            		// We have a question
            		QuestionResponseJAXB.Languages.Language.Receipt receipt = language.getReceipt() ;

            		if (receipt != null)
            			setReceiptMessage(receipt.getNotratedvalue());
            	}
            }
        } catch (Exception e) {
            log.error("Error fetching the next question", e);
        }
        
        return questionResponseJAXB;         
    }
    
    private void runQuestion(ITruModuleProperties properties, QuestionResponseJAXB questionResponseJAXB) {
        String keyStroke = String.valueOf(NO_QUESTION_ASKED) ;
        long totalTimeTaken = 0; 
        try {        	
        	final QuestionResponseJAXB.Languages.Language language = 
        			getLanguageManager().getLanguage(questionResponseJAXB, properties.getLanguageCode()) ;
	        final Question question = language.getDisplayElements().getQuestion();
	        
            final int displayWidth = properties.getDeviceCpl();
	        String qText = question.getValue() ;
	        if (qText != null) {
	            String[] qTextWraps = qText.split("\\\\n") ;
	            if ((qTextWraps.length == 1) && (qTextWraps[0].length() > displayWidth))
	            	qTextWraps = StringUtilities.wordWrap(qText, displayWidth);
	
		        int timeout = properties.getQuestionTimeout() ;
		        if (timeout < 1000)
		        	timeout = 60000 ;
	            
		        final long startTime = System.currentTimeMillis();
		        keyStroke = iDevice.displayTruratingQuestionGetKeystroke(qTextWraps, qText, timeout);
		        final long endTime = System.currentTimeMillis();
		        totalTimeTaken = endTime - startTime;
	        }
	        //if user rated then check if there is a prize
	        Rating rating = getRatingRecord(properties).getRating();
	        if ((new Integer(keyStroke) > 0)) {
	        	// Update the receipt text to indicate that the user rated
	        	final QuestionResponseJAXB.Languages.Language.Receipt receipt = language.getReceipt() ;
	        	setReceiptMessage(receipt.getRatedvalue());	        
	        	rating.setPrizecode(checkForPrize.checkForAPrize(getDevice(), questionResponseJAXB, properties.getLanguageCode()));
	        }
	        rating.setValue(new Short(keyStroke));
	        rating.setResponsetimemilliseconds(totalTimeTaken);
	        rating.setQid(question.getQid());
	
	    } catch (Exception e) {
	        log.error("truModule error", e);
	    }	    	
    }

	/** 
	 * The message that should appear on the receipt as a consequence
	 * of the outcome of this rating question
	 */
	public String getReceiptMessage() {
		return receiptMessage;
	}
	private void setReceiptMessage(String receiptMessage) {
		this.receiptMessage = receiptMessage;
	}
	
    private void endTransaction() {
    	// Clear the current transaction
    	currentRatingRecord = null ;   
    	receiptMessage = "" ;    	
    }
    
	public void close() {
		endTransaction() ;
		xmlNetworkMessenger.close();
	}
}
