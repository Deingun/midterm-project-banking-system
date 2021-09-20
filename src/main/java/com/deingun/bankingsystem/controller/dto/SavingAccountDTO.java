package com.deingun.bankingsystem.controller.dto;


import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SavingAccountDTO {

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
    @NotEmpty(message = "Secret Key must be provided")
    private String secretKey;
    @DecimalMin(value = "100", message = "The minimum balance of a savings account cannot be less than 100")
    private String minimumBalance;
    @DecimalMax(value = "0.5")
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(String minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }
}
