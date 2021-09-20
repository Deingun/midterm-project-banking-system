package com.deingun.bankingsystem.controller.dto;

import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.utils.Money;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

public class TransactionDTO {

    @NotEmpty(message = "Origin Account must be provided")
    private String originAccount;
    @NotEmpty(message = "Destination Account must be provided")
    private String destinationAccount;

    private String accountNumber;
    @NotEmpty(message = "Amount must be provided")
    private String amount;
    private String hashedKey;

    private String secretKey;
    private String transactionType;


    public String getOriginAccount() {
        return originAccount;
    }

    public void setOriginAccount(String originAccount) {
        this.originAccount = originAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
