package com.deingun.bankingsystem.model.account;

import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.user.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
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
    @Column(name = "credit_limit")
    @DecimalMax(value = "100000")
    private BigDecimal creditLimit;
    @Column(name = "interest_rate")
    @DecimalMin(value = "0.1")
    private Float interestRate;

    public CreditCardAccount() {
    }

    public CreditCardAccount(String entityNumber, String branchNumber, BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal creditLimit, Float interestRate) {
        super(entityNumber, branchNumber, balance);
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCardAccount(String entityNumber, String branchNumber, BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Float interestRate) {
        super(entityNumber, branchNumber, balance);
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creditLimit = new BigDecimal("100");
        this.interestRate = interestRate;
    }

    public CreditCardAccount(String entityNumber, String branchNumber, BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal creditLimit) {
        super(entityNumber, branchNumber, balance);
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creditLimit = creditLimit;
        this.interestRate = 0.2F;
    }

    public CreditCardAccount(String entityNumber, String branchNumber, BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(entityNumber, branchNumber, balance);
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creditLimit = new BigDecimal("100");
        this.interestRate = 0.2F;
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
