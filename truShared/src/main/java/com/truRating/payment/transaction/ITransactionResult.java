package com.trurating.payment.transaction;

/**
 * Created by Paul on 22/03/2016.
 */
public interface ITransactionResult {

    String getTransactionResult();

    void setTransactionResult(String transactionResult);

    String getTransactionNumber();

    void setTransactionNumber(String transactionNumber);

    String getTenderType();

    void setTenderType(String tenderType);

    String getCardType();

    void setCardType(String cardType);
}
