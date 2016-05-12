package com.trurating.payment;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.trurating.network.xml.TruRatingMessageFactory;

/**
 * Created by Paul on 10/03/2016.
 */
public class PaymentResponse implements IPaymentResponse {

    private Logger log = Logger.getLogger(TruRatingMessageFactory.class);
    private String operatorId = "";
    private long transactionNumber = 0;
	String transactionReference = "";
    private String transactionDate = "";
    private String transactionTime = "";
	private long transactionAmount = 0;
	TransactionType transactionType = TransactionType.ISO_PURCHASE ;
    private TenderType tenderType = TenderType.UNKNOWN;
    private TransactionResult transactionResult = TransactionResult.UNKNOWN;
    private String cardScheme = "";
    private String cardHashType = "";
    private String cardHashData = "";
	private int errorCode = 0;
	private String errorMessage = "";
    private String terminalID="";

    public PaymentResponse() {
		Date now = new Date() ;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		transactionDate = sdf.format(now);
		
		sdf = new SimpleDateFormat("HH:mm:ss");
		transactionTime = sdf.format(now);
	}
	
	
	/**
	 * Transaction number, unique to point of service
	 */
    public long getTransactionId() {
        return transactionNumber;
    }

    public void setTransactionId(long value) {
        transactionNumber = value;
    }

    public String getTransactionDate() {
        return transactionDate.toString();
    }

    public void setTransactionDate(String date) {
        this.transactionDate = date;
    }

    public void setTransactionAmount(Long aLong) {

    }

    public void setCurrencyCode(Integer integer) {

    }

    public void setTransactionNumber(Integer integer) {

    }

    public String getTransactionTime() {
        return transactionTime.toString();
    }

    public void setTransactionTime(String time) {
        this.transactionTime = time;
    }

	/**
	 * Unique (for this terminal) code identifying this transaction
	 */
	public String getTransactionReference() {
		return transactionReference ;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}


	/**
	 * Transaction type - goods / services / cashback etc
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(TransactionType type) {
		transactionType = type;		
	}
    
	/**
	 * Approved transaction amount in lowest denomination of the given currency
	 */
    public long getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(long value) {
    	transactionAmount = value ;
    }

    public long getCashbackAmount() {
        return 0;
    }

    public void setCashbackAmount(long value) {

    }

    public int getCurrencyCode() {
        return 0;
    }

    public void setCurrencyCode(int value) {

    }

    public String getMerchantNumber() {
        return null;
    }

    public void setMerchantNumber(String value) {

    }

    public String getTerminalId() {
        return terminalID;
    }

    public void setTerminalId(String terminalID) {
        this.terminalID= terminalID;
    }

    public TenderType getTenderType() {
        return tenderType;
    }

    public void setTenderType(TenderType value) {
        this.tenderType = value;
    }

    public String getCardScheme() {
        return cardScheme;
    }

    public void setCardScheme(String value) {
        this.cardScheme = value;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }


    public String getOperatorId() {
        return operatorId;
    }


    public void setCardHashType(String type) {
        cardHashType = type;
    }

    public String getCardHashType() {
        return cardHashType;
    }

    public String getCardHashData() {
        return cardHashData;
    }
    
    public void setCardHashData(String value) {
        cardHashData = value;
    }
    
    public void setTransactionResult(TransactionResult transactionStatusCode) {
        this.transactionResult = transactionStatusCode;
    }

    public TransactionResult getTransactionResult() {
        return transactionResult;
    }

    /**
	 * Flag to indicate whether the transaction was approved
	 * 
	 */
    public boolean isApproved() {
        return transactionResult.equals(TransactionResult.APPROVED);
    }

	/**
	 * Flag to indicate whether the transaction was cancelled
	 * 
	 */
    public boolean isCancelled() {
        return transactionResult.equals(TransactionResult.CANCELLED);
    }

	/**
	 * Error code
	 */
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int value) {
    	errorCode = value ;
    }

	/**
	 * Error message
	 */
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String value) {
    	errorMessage = value;
    }
}
