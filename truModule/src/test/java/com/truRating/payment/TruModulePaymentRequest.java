package com.trurating.payment;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.trurating.payment.IPaymentRequest;

/**
 * Created by Paul on 10/03/2016.
 */
public class TruModulePaymentRequest implements IPaymentRequest {

    private Logger log = Logger.getLogger(TruModulePaymentRequest.class);
    private String product ;
    private long price;
    
    
    public TruModulePaymentRequest(String product, long price) {
    	this.product = product;
    	this.price=price;
    }
    
	public long getTransactionValue() {
		// TODO Auto-generated method stub
		return price;
	}

	public long getTenderValue() {
		// TODO Auto-generated method stub
		return price;
	}

	public long getCashbackAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getProductCode() {
		// TODO Auto-generated method stub
		return product;
	}

	public String getTransactionReference() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTransactionReference(String transactionReference) {
		// TODO Auto-generated method stub
		
	}

	public int getCurrencyCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setCurrencyCode(int code) {
		// TODO Auto-generated method stub
		
	}

	public String getCurrencySymbol() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCurrencyExponent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLanguageCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTerminalId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOperatorId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOperatorId(String id) {
		// TODO Auto-generated method stub
		
	}

	public void setProperty(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public TenderType getTenderType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTenderType(TenderType value) {
		// TODO Auto-generated method stub
		
	}

	public TransactionType getTransactionType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBasket() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBasket(String basket) {
		// TODO Auto-generated method stub
		
	}

	public String getProduct() {
		// TODO Auto-generated method stub
		return product;
	}

	public void setProduct(String product) {
		// TODO Auto-generated method stub
		
	}

	public String getOperatorID() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOperatorID(String operatorID) {
		// TODO Auto-generated method stub
		
	}
}
