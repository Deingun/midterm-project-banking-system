package com.deingun.bankingsystem.model.account;

import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.user.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "account_id")
@Table(name = "credit_card_account")
public class CreditCardAccount extends Account{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "primary_owner_id")
    private AccountHolder primaryOwner;
    @ManyToOne
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;
    @Column(name = "penalty_fee")
    private BigDecimal penaltyFee;
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    @Column(name = "interest_rate")
    private Float interestRate;

    public CreditCardAccount() {
    }

    public CreditCardAccount(int entity, int branch, BigDecimal balance, BigDecimal penaltyFee, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee1, BigDecimal creditLimit, Float interestRate) {
        super(entity, branch, balance, penaltyFee);
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = penaltyFee1;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    @Override
    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    @Override
    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Float interestRate) {
        this.interestRate = interestRate;
    }
}
