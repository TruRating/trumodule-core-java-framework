package com.trurating.trumodule.testharness.device;

import com.trurating.payment.TenderType;
import com.trurating.payment.TransactionResult;
import com.trurating.payment.TransactionType;

/**
 * Created by Dave on 06/05/2016.
 */
public interface IPaymentResponse {

    /**
     * Unique merchant number
     */
    String getMerchantNumber();

    /**
     * Unique terminal number
     */
    String getTerminalId();

    String getOperatorId();

    /**
     * POS application's transaction reference
     */
    String getTransactionReference();

    /**
     * Transaction date
     */
    String getTransactionDate();

    /**
     * Transaction time
     */
    String getTransactionTime();

    /**
     * Error code
     */
    int getErrorCode();

    /**
     * Error code
     */
    void setErrorCode(int value);

    /**
     * Error Message associated with Error Code
     */
    String getErrorMessage();

    /**
     * Error Message associated with Error Code
     */
    void setErrorMessage(String value);

    /**
     * Transaction type (Sales, Refund, Cash, etc.)
     */
    TransactionType getTransactionType();

    /**
     * Tender Type (Cash, Swiped, Keyed, SmartCard, CustomerNotPresent, Contactless)
     */
    TenderType getTenderType();

    /**
     * Transaction amount in lowest denomination
     */
    long getTransactionAmount();

    /**
     * Cashback element of transaction amount
     */
    long getCashbackAmount();

    /**
     * Currency code for transaction amount
     */
    int getCurrencyCode();

    String getCardHashType();

    String getCardHashData();

    TransactionResult getTransactionResult();

    void setTransactionResult(TransactionResult transactionResult);

    String getCardScheme();
}