package com.deingun.bankingsystem.model.account;

import com.deingun.bankingsystem.model.user.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@MappedSuperclass
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "entity", nullable = false)
    private int entity;
    @Column(name = "branch", nullable = false)
    @NotEmpty(message = "Branch must be provided")
    private int branch;
    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "penalty_fee")
    private BigDecimal penaltyFee;

    public Account() {
    }

    public Account(int entity, int branch, BigDecimal balance, BigDecimal penaltyFee) {
        this.entity = entity;
        this.branch = branch;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }
}
