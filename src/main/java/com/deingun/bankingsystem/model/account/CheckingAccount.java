package com.deingun.bankingsystem.model.account;

import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.user.AccountHolder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@PrimaryKeyJoinColumn(name = "account_id")
@Table(name = "checking_account")
public class CheckingAccount extends Account{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "secret_key", nullable = false)
    @NotEmpty(message = "Secret Key must be provided")
    private String secretKey;
    @Column(name = "minimum_balance")
    private BigDecimal minimumBalance;
    @Column(name = "monthly_maintenance_fee")
    private BigDecimal monthlyMaintenanceFee;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public CheckingAccount() {
    }

    public CheckingAccount(int entity, int branch, BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal penaltyFee, String secretKey, BigDecimal minimumBalance, BigDecimal monthlyMaintenanceFee, LocalDate creationDate, Status status) {
        super(entity, branch, balance, primaryOwner, secondaryOwner, penaltyFee);
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.creationDate = creationDate;
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Status getStatus() {
        return status;
    }
}
