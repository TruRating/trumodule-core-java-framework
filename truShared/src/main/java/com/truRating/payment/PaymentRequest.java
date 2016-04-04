package com.trurating.payment;

import com.trurating.payment.TenderType;
import com.trurating.payment.TransactionType;
import com.trurating.payment.IPaymentRequest;

import java.math.BigDecimal;

/**
 * Created by Paul on 01/03/2016.
 */
public class PaymentRequest implements IPaymentRequest {

    private Double transactionValue = new Double(0.0);
    private String product = "";
    private TenderType tenderType;
    private String operatorID;
    private String transactionReference;
    private int currencyCode;
    private String basket;

    public PaymentRequest(Double transactionValue) {
        this.transactionValue = transactionValue;
    }

    public PaymentRequest(String product, BigDecimal amount) {
        this.product = product;
        this.transactionValue =amount.doubleValue();
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public long getTransactionValue() {
        return transactionValue.longValue();
    }

    
    public long getTenderValue() {
        return 0;
    }

    
    public long getCashbackAmount() {
        return 0;
    }

    
    public String getProductCode() {
        return null;
    }

    
    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return null;
    }

    
    public int getCurrencyExponent() {
        return 0;
    }

    
    public String getLanguageCode() {
        return null;
    }

    
    public String getTerminalId() {
        return null;
    }

    
    public String getOperatorId() {
        return operatorID;
    }

    public void setOperatorId(String operatorID) {
        this.operatorID=operatorID;
    }
    
    public void setProperty(String key, String value) {

    }

    
    public String getProperty(String key) {
        return null;
    }

    
    public TenderType getTenderType() {
        return tenderType;
    }

    
    public void setTenderType(TenderType value) {
        this.tenderType = value;
    }

    public TransactionType getTransactionType() {
        return null;
    }

    public String getBasket() {
        return basket;
    }

    public void setBasket(String basket) {
        this.basket=basket;
    }
}
