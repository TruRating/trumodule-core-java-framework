package com.trurating;

import org.apache.log4j.Logger;

import com.trurating.device.IDevice;
import com.trurating.network.xml.TruRatingMessageFactory;
import com.trurating.network.xml.XMLNetworkMessenger;
import com.trurating.prize.PrizeManager;
import com.trurating.properties.ITruModuleProperties;
import com.trurating.util.StringUtilities;
import com.trurating.xml.questionResponse.QuestionResponseJAXB;
import com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.DisplayElements.Question;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB;
import com.trurating.xml.ratingDelivery.RatingDeliveryJAXB.Rating;
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
    private String receiptMessage = "";
    
    private volatile RatingDeliveryJAXB currentRatingRecord = null ;
    
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
	public RatingDeliveryJAXB getRatingRecord(ITruModuleProperties properties) {
		if (currentRatingRecord == null)
			currentRatingRecord = new TruRatingMessageFactory().createRatingRecord(properties) ;
		return currentRatingRecord;
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
            else if ((questionResponseJAXB != null) && (questionResponseJAXB.getLanguages().getLanguage().getDisplayElements().getQuestion().getValue().length() > 0)) {	
            	// We have a question
            	final com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.Receipt receipt = 
            			questionResponseJAXB.getLanguages().getLanguage().getReceipt() ;
            	
            	if (receipt != null)
            		setReceiptMessage(receipt.getNotratedvalue());	        
            }
        } catch (Exception e) {
            log.error("Error fetching the next question", e);
        }
        
        return questionResponseJAXB;         
    }
    
    private void runQuestion(ITruModuleProperties properties, QuestionResponseJAXB questionResponseJAXB) {
        String keyStroke;
        try {        	
	        final Question question = questionResponseJAXB.getLanguages().getLanguage().getDisplayElements().getQuestion();
	        
            final int displayWidth = properties.getDeviceCpl();
	        String qText = question.getValue() ;
            String[] qTextWraps = qText.split("\\\\n") ;
            if ((qTextWraps.length == 1) && (qTextWraps[0].length() > displayWidth))
            	qTextWraps = StringUtilities.wordWrap(qText, displayWidth);

	        int timeout = properties.getQuestionTimeout() ;
	        if (timeout < 1000)
	        	timeout = 60000 ;
            
	        final long startTime = System.currentTimeMillis();
	        keyStroke = iDevice.displayTruratingQuestionGetKeystroke(qTextWraps, qText, timeout);
	        final long endTime = System.currentTimeMillis();
	        final Long totalTimeTaken = endTime - startTime;
	
	        //if user rated then check if there is a prize
	        Rating rating = getRatingRecord(properties).getRating();
	        if ((new Integer(keyStroke) > 0)) {
	        	// Update the receipt text to indicate that the user rated
	        	final com.trurating.xml.questionResponse.QuestionResponseJAXB.Languages.Language.Receipt receipt = 
	        			questionResponseJAXB.getLanguages().getLanguage().getReceipt() ;
	            setReceiptMessage(receipt.getRatedvalue());	        
	            rating.setPrizecode(checkForPrize.checkForAPrize(getDevice(), questionResponseJAXB));
	        }
	        rating.setValue(new Short(keyStroke));
	        rating.setResponsetimemilliseconds(totalTimeTaken);
	        rating.setQid(question.getQid());
	
	    } catch (Exception e) {
	        log.error("truModule error", e);
	    }	    	
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

	public void cancelRating() {
        getDevice().cancelInput();
    }

    public boolean deliverRating(ITruModuleProperties properties) {

        try {
        	if (currentRatingRecord == null)
        		log.error("Call to deliverRating when no rating record exists!");

        	else {
        		//send the rating
        		final RatingResponseJAXB ratingResponseJAXB = xmlNetworkMessenger.deliverRatingToService(currentRatingRecord);
        		final Receipt ratingResponseReceipt = ratingResponseJAXB.getLanguages().getLanguage().getReceipt();

        		if (currentRatingRecord.getRating().getValue() > 0)
        			setReceiptMessage(ratingResponseReceipt.getRatedvalue());
        		else
        			setReceiptMessage(ratingResponseReceipt.getNotratedvalue());
        	}
        } catch (Exception e) {
            log.error(e);
            return false;
        }
        finally {
        	endTransaction() ;
        }        

        log.info("Everything is finished in truModule!");
        return true;
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
