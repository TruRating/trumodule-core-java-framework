/**
 * 
 */
package com.trurating.payment;

/**
 * @author Peter Salmon
 *
 */
public class PaymentRequest implements IPaymentRequest {

	/**
	 * Terminal Id
	 */
	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String newValue) {
		terminalId = newValue;
	}

	String terminalId = "" ;
	
	/**
	 * Operator Id - the Id of the till attendant
	 */
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String id) {
		operatorId = id;
	}
	String operatorId = "";

	
	/**
	 * Unique (for this terminal) code identifying this transaction
	 */
	public String getTransactionReference() {
		return transactionReference ;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}
	String transactionReference = "";

	/**
	 * Transaction type - goods / services / cashback etc
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(TransactionType type) {
		transactionType = type;		
	}
	TransactionType transactionType = TransactionType.ISO_PURCHASE ;
	
	/**
	 * Tender Type (Cash, Swiped, Keyed, SmartCard, CustomerNotPresent, Contactless)
	 */
	public TenderType getTenderType() {
		return tenderType;
	}

	public void setTenderType(TenderType value) {
		tenderType = value;
	}
	
	TenderType tenderType = TenderType.SMARTCARD ;
	
	/**
	 * The total transaction amount in the lowest denomination of the given currency
	 */
	public long getTransactionValue() {
		return transactionValue ;
	}

	public void setTransactionValue(long newValue) {
		transactionValue = newValue;
	}
	long transactionValue = 0;
	
	/**
	 * The amount requested for this tender forming part of the transaction, 
	 * in the lowest denomination of the given currency
	 */
	public long getTenderValue() {
		return tenderValue ;
	}

	public void setTenderValue(long newValue) {
		tenderValue = newValue;
	}
	long tenderValue = 0;

	/**
	 * The amount of any cashback requested 
	 */
	public long getCashbackAmount() {
		return cashbackAmount ;
	}

	public void setCashbackAmount(long newValue) {
		cashbackAmount = newValue;
	}
	long cashbackAmount = 0;

	
	/**
	 * Code to identify the product category
	 */
	public String getProductCode() {
		return productCode ;
	}

	public void setProductCode(String id) {
		productCode = id;
	}
	String productCode = "";

	/**
	 * The currency code the transaction is to be conducted in
	 */
	public int getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(int code) {
		currencyCode = code;
	}
	int currencyCode = 826;

	/**
	 * Returns the ISO 639-1 code for language that this transaction is being
	 * conducted in Ideally this would be set by the cardholder's preferred
	 * language - as identified by the card although there may be a
	 * synchronisation issue if the card dialog cannot start until after the
	 * TruRating app has completed
	 */
	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String code) {
		languageCode = code;
	}
	
	String languageCode = "en";
	
	/**
	 *	A string representation of the basket contents 
	 */
	public String getBasket() {
		return basket;
	}
	public void setBasket(String basket) {
		this.basket = basket;
	}
	String basket = "";
}
