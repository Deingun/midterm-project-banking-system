package com.deingun.bankingsystem.controller.dto;

import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.utils.Money;

import javax.persistence.*;

public class TransactionDTO {

    private String originAccount;
    private String destinationAccount;
    private String amount;

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
}
