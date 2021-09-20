package com.deingun.bankingsystem.controller.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreditCardAccountDTO {

    @NotEmpty(message = "Entity must be provided")
    private String entityNumber;
    @NotEmpty(message = "Branch must be provided")
    private String branchNumber;
    @NotEmpty(message = "Amount must be provided")
    private String amount;
    private String currency;
    @NotNull(message = "Primary Owner must be provided")
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    @DecimalMax(value = "100000",message = "The credit limit cannot exceed 100,000.00")
    private String creditLimit;
    @DecimalMin(value = "0.1",message = "The interest rate cannot be less than 0.1")
    private String interestRate;

    public String getEntityNumber() {
        return entityNumber;
    }

    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }

    public String getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(String branchNumber) {
        this.branchNumber = branchNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(Long primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public Long getSecondaryOwnerId() {
        return secondaryOwnerId;
    }

    public void setSecondaryOwnerId(Long secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }
}
