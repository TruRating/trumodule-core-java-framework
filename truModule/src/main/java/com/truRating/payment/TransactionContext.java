package com.trurating.payment;

import com.trurating.rating.Rating;

public class TransactionContext {

    private String transactionId = null;
    private Rating rating = null ; 
    private IPaymentRequest paymentRequest = null ; 
    private IPaymentResponse paymentResponse = null ;
    private String receiptMessage = "";
    
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		
		// checkTransactionId and auto assign if empty		
		this.transactionId = transactionId;
	}

	public Rating getRating() {
		return rating;
	}
	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public IPaymentRequest getPaymentRequest() {
		return paymentRequest;
	}
	public void setPaymentRequest(IPaymentRequest paymentRequest) {
		this.paymentRequest = paymentRequest;
	}
	
	public IPaymentResponse getPaymentResponse() {
		return paymentResponse;
	}
	public void setPaymentResponse(IPaymentResponse paymentResponse) {
		this.paymentResponse = paymentResponse;
	}
	public String getReceiptMessage() {
		return receiptMessage;
	}
	public void setReceiptMessage(String receiptMessage) {
		this.receiptMessage = receiptMessage;
	}    
}
