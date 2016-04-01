package com.truRating.paymentApplication;

import org.apache.log4j.Logger;

import com.truRating.truModule.network.xml.TruRatingMessageFactory;
import com.truRating.truModule.payment.IPaymentResponse;
import com.truRating.truModule.payment.TenderType;
import com.truRating.truModule.payment.TransactionStatusCode;

/**
 * Created by Paul on 10/03/2016.
 */
public class TruModulePaymentResponse implements IPaymentResponse {

    private Logger log = Logger.getLogger(TruRatingMessageFactory.class);
    private String cardScheme = "";
    private boolean transactionIsApproved;
    private TenderType tenderType;
    private String operatorId;
    private String cardHashType;
    private String cardHashData;
    private String transactionDate;
    private String transactionTime;
    private int transactionNumber;
    private TransactionStatusCode transactionStatusCode;

    public boolean isApproved() {
        return transactionIsApproved;
    }

    public boolean isCancelled() {
        return false;
    }

    public int getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(int value) {
        transactionNumber = value;
    }

    public String getTransactionDate() {
        return transactionDate.toString();
    }

    public void setTransactionDate(String date) {
        this.transactionDate = date;
    }

    public String getTransactionTime() {
        return transactionTime.toString();
    }

    public void setTransactionTime(String time) {
        this.transactionTime = time;
    }

    public int getErrorCode() {
        return 0;
    }

    public void setErrorCode(int value) {

    }

    public String getErrorMessage() {
        return null;
    }

    public void setErrorMessage(String value) {

    }

    public long getTransactionAmount() {
        return 0;
    }

    public void setTransactionAmount(long value) {

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
        return null;
    }

    public void setTerminalId(String value) {

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

    public void setTransactionIsApproved() {
        transactionIsApproved = true;
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

    public void setResult(TransactionStatusCode transactionStatusCode) {
        this.transactionStatusCode = transactionStatusCode;
    }

    public TransactionStatusCode getResult() {
        return transactionStatusCode;
    }

    public void setCardHashData(String value) {
        cardHashData = value;
    }

}
