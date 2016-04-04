

package com.trurating.payment.transaction;

public class TransactionResult implements ITransactionResult {

	private String transactionResult="";
	private String transactionNumber="";
	private String tenderType="";
	private String cardType="";

    public String getTransactionResult() {
		return transactionResult;
	}

    public void setTransactionResult(String transactionResult) {
		this.transactionResult = transactionResult;
	}

    public String getTransactionNumber() {
		return transactionNumber;
	}

    public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

    public String getTenderType() {
		return tenderType;
	}

    public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}

    public String getCardType() {
		return cardType;
	}

    public void setCardType(String cardType) {
		this.cardType = cardType;
	}
}
