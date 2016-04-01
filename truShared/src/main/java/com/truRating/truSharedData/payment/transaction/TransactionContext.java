package com.truRating.truSharedData.payment.transaction;

import com.truRating.truModule.payment.IPaymentRequest;
import com.truRating.truModule.payment.IPaymentResponse;

public class TransactionContext implements ITransactionContext {

	IPaymentRequest request;

	public TransactionContext(IPaymentRequest request) {
		this.request = request;
	}

	public IPaymentRequest getRequest() {
		return this.request;
	}

	// Nothing to do
	public IPaymentResponse getResponse() {
		return null;
	}

	// Nothing to do
	public void setResponse(IPaymentResponse response) {
	}

	// Nothing to do
	public String getOperator() {
		return "";
	}

	// Nothing to do
	public void setOperator(String operator) {
	}

	// Nothing to do
	public Object getFieldValue(String field) {
		return null;
	}

	// Nothing to do
	public void setFieldValue(String field, Object value) {
	}
}
