package com.trurating;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.trurating.properties.TruModuleProperties;
import com.trurating.util.StringUtilities;
import org.apache.log4j.Logger;

import com.trurating.device.IDevice;
import com.trurating.network.xml.XMLNetworkMessenger;
import com.trurating.payment.IPaymentRequest;
import com.trurating.payment.IPaymentResponse;
import com.trurating.payment.TransactionContext;
import com.trurating.prize.PrizeManager;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.rating.Rating;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.DisplayElements.Question;
import com.trurating.xml.ratingResponse.RatingResponseJAXB;
import com.trurating.xml.ratingResponse.RatingResponseJAXB.Languages.Language.Receipt;

/**
 * Created by Paul on 01/03/2016.
 */
public class TruModule implements ITruModule  {

    private final Logger log = Logger.getLogger(TruModule.class);

    private IDevice iDevice = null ;

    private XMLNetworkMessenger xmlNetworkMessenger = null ;;    
    private final PrizeManager checkForPrize = new PrizeManager();
    
    private volatile TransactionContext currentTransactionContext = null ;
    
    public TruModule() {
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
	public TransactionContext getTransactionContext() {
		if (currentTransactionContext == null)
			currentTransactionContext = new TransactionContext() ;
		return currentTransactionContext;
	}
	
    private QuestionResponseJAXB getNextQuestion(ITruModuleProperties properties, String transactionId) {
    	QuestionResponseJAXB questionResponseJAXB = null ;

        try {        	
            questionResponseJAXB = xmlNetworkMessenger.getQuestionFromService(properties, transactionId);
            
            if (questionResponseJAXB.getErrortext() != null) {
                if (!questionResponseJAXB.getErrortext().equals("")) {
                    log.error("The QuestionRequest produced an error : " + questionResponseJAXB.getErrortext());
                    return null;
                }
            }
            else if (questionResponseJAXB != null) {	// We have a question
            	final com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.Receipt receipt = 
            			questionResponseJAXB.getLanguages().getLanguage().getReceipt() ;
            	
            	if (receipt != null)
            		getTransactionContext().setReceiptMessage(receipt.getNotratedvalue());	        
            }
        } catch (Exception e) {
            log.error("Error fetching the next question", e);
        }
        
        return questionResponseJAXB;         
    }
    
    private void runQuestion(QuestionResponseJAXB questionResponseJAXB, ITruModuleProperties properties) {
        String keyStroke;
        try {        	
	        final Question question = questionResponseJAXB.getLanguages().getLanguage().getDisplayElements().getQuestion();
	        
	        String qText = question.getValue() ;
	        final long startTime = System.currentTimeMillis();
            final int displayWidth = properties.getDeviceCpl();

            String[] qTextWraps = StringUtilities.wordWrap(qText, displayWidth);
	        keyStroke = iDevice.displayTruratingQuestionGetKeystroke(qTextWraps, qText, 60*1000); //qText.split("\\\\n")
	        final long endTime = System.currentTimeMillis();
	        final Long totalTimeTaken = endTime - startTime;
	
	        //if user rated then check if there is a prize
	        String prizeCode = "";
	        if ((new Integer(keyStroke) > 0)) {
	        	// Update the receipt text to indicate that the user rated
	        	final com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.Receipt receipt = 
	        			questionResponseJAXB.getLanguages().getLanguage().getReceipt() ;
	            getTransactionContext().setReceiptMessage(receipt.getRatedvalue());	        
	        	
	            prizeCode = checkForPrize.checkForAPrize(getDevice(), questionResponseJAXB, prizeCode);
	        }
	
	        Rating rating = new Rating();
	        rating.setValue(new Short(keyStroke));
	        rating.setTimeTakeToRate(totalTimeTaken.toString());
	        if (prizeCode == null) prizeCode = ""; //for unlikely event of prizeCode check method failing
	        rating.setPrizeCode(prizeCode);
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        rating.setRatingDateTime(sdf.format(new Date()));
	        rating.setQuestionID(new Long(question.getQid()).intValue());
	        getTransactionContext().setRating(rating);
	
	    } catch (Exception e) {
	        log.error("truModule error", e);
	    }	    	
    }

    /**
     * Request to run a rating question
     * If this needs to be done in a background thread then the assumption is
     * that the thread will already have been created and this call will have been made from it.
     */
    public void doRating(ITruModuleProperties properties, IPaymentRequest paymentRequest) {
    	String currentTransactionId = paymentRequest.getTransactionReference();
    	getTransactionContext().setTransactionId(currentTransactionId) ;
    	getTransactionContext().setPaymentRequest(paymentRequest);
    	QuestionResponseJAXB jaxbQuestion = getNextQuestion(properties, getTransactionContext().getTransactionId()) ;
    	runQuestion (jaxbQuestion, properties) ;
    }

	/**
	 * 	Dwell time ratings questions need to be run in a separate thread
	 */	
    public void doRatingInBackground(final ITruModuleProperties properties, String transactionId) {
    	getTransactionContext().setTransactionId(transactionId) ;
    	final QuestionResponseJAXB jaxbQuestion = getNextQuestion(properties, transactionId) ;

    	new Thread(new Runnable() 
    			{
    				public void run() { 
    					runQuestion (jaxbQuestion, properties) ;
    				}
    			}
    	).start() ;
    }

	public void cancelRating() {
        getDevice().cancelInput();
    }


    public boolean recordRatingResponse(ITruModuleProperties properties, IPaymentResponse paymentResponse) {

        try {
            Rating rating = getTransactionContext().getRating();
            getTransactionContext().setPaymentResponse(paymentResponse);

            //send the rating
            final RatingResponseJAXB ratingResponseJAXB = xmlNetworkMessenger.deliveryRatingToService(properties, 
            		getTransactionContext().getTransactionId(), rating, getTransactionContext().getPaymentResponse());

            final Receipt ratingResponseReceipt = ratingResponseJAXB.getLanguages().getLanguage().getReceipt();

            boolean userRated = false;
            if (rating != null) {
                if (new Integer(rating.getValue()) > 0) 
                	userRated = true;
            }
        	
        	if (userRated)
            	getTransactionContext().setReceiptMessage(ratingResponseReceipt.getRatedvalue());
        	else
        		getTransactionContext().setReceiptMessage(ratingResponseReceipt.getNotratedvalue());
            
        } catch (Exception e) {
            log.error(e);
            return false;
        }
        finally {
        	// Clear the current transaction
        	currentTransactionContext = null ;       	
        }        

        log.info("Everything is finished in truModule!");
        return true;
    }
}
