package com.trurating.rating;


import com.trurating.payment.TenderType;

/**
 * Created by Paul on 08/03/2016.
 */
public class Rating {
    private int value;
    private String ratingTime;
    private String prizeCode;
    private String ratingDateTime;
    private int questionID;


    public String toRecordString() {
        return null;
    }


    public long getTransactionNumber() {
        return 0;
    }


    public void setTransactionNumber(long transactionNumber) {
    }


    public String getTransactionId() {
        return null;
    }


    public void setTransactionId(String newVal) {

    }


    public String getOperatorID() {
        return null;
    }


    public void setOperatorID(String operatorID) {

    }


    public String getRatingDate() {
        return null;
    }


    public void setRatingDate(String ratingDate) {

    }


    public String getRatingTime() {
        return ratingTime;
    }


    public void setRatingTime(String ratingTime) {
        this.ratingTime = ratingTime;
    }


    public String getRatingDateTime() {
        return ratingDateTime;
    }


    public void setRatingDateTime(String ratingDateTime) {
        this.ratingDateTime = ratingDateTime;
    }


    public int getQuestionID() {
        return questionID;
    }


    public void setQuestionID(int questionID) {
        this.questionID=questionID;
    }


    public String getLanguageCode() {
        return null;
    }


    public void setLanguageCode(String languageCode) {

    }


    public int getValue() {
        return value;
    }


    public void setValue(int value) {
        this.value = value;
    }


    public String getProductCode() {
        return null;
    }


    public void setProductCode(String productCode) {

    }


    public long getTxnAmount() {
        return 0;
    }


    public void setTxnAmount(long txnAmount) {

    }


    public long getGratuity() {
        return 0;
    }


    public void setGratuity(long gratuity) {

    }


    public String getToken() {
        return null;
    }


    public void setToken(String token) {

    }


    public String getTerminalID() {
        return null;
    }


    public void setTerminalID(String terminalID) {

    }


    public String getPrizeCode() {
        return prizeCode;
    }


    public void setPrizeCode(String prizeCode) {
        this.prizeCode=prizeCode;
    }


    public boolean wasTransactionApproved() {
        return false;
    }

//
//    public ITransactionResult getITransactionResult() {
//        return null;
//    }
//
//
//    public void setITransactionResult(ITransactionResult result) {
//
//    }


    public int getPrizeCountDown() {
        return 0;
    }


    public void setPrizeCountDown(int count) {

    }


    public int getQRegulationSecs() {
        return 0;
    }


    public void setQRegulationSecs(int secs) {

    }


    public String getQuestionSequence() {
        return null;
    }


    public void setQuestionSequence(String s) {

    }


    public int getQuestionPointer() {
        return 0;
    }


    public void setQuestionPointer(int i) {

    }


    public int getCurrencyCode() {
        return 0;
    }


    public void setCurrencyCode(int txnAmount) {

    }


    public TenderType getITenderType() {
        return null;
    }


    public void setITenderType(TenderType value) {

    }
}
