package com.deingun.bankingsystem.model.account;

import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.utils.Address;
import com.deingun.bankingsystem.utils.Money;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Inheritance(strategy=InheritanceType.JOINED)
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "entity_number", nullable = false)
    private String entityNumber;
    @Column(name = "branch_number", nullable = false)
    @NotEmpty(message = "Branch must be provided")
    private String branchNumber;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency")),
    })
    private Money balance;

    @Column(name = "penalty_fee")
    private final BigDecimal PENALTYFEE = new BigDecimal("40");


    @Column(name = "account_number")
    private String accountNumber;

    public Account() {
    }

    public Account(String entityNumber, String branchNumber, Money balance) {
        this.entityNumber = entityNumber;
        this.branchNumber = branchNumber;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public BigDecimal getPenaltyFee() {
        return PENALTYFEE;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
