package com.deingun.bankingsystem.model;

import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.user.User;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "origin_account_id")
    private Account originAccountId;
    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccountId;
    @ManyToOne
    @JoinColumn(name = "paymaster_id")
    private User paymasterId;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiverId;

    public Transaction() {
    }

    public Transaction(Account originAccountId, Account destinationAccountId, User paymasterId, User receiverId) {
        this.originAccountId = originAccountId;
        this.destinationAccountId = destinationAccountId;
        this.paymasterId = paymasterId;
        this.receiverId = receiverId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getOriginAccountId() {
        return originAccountId;
    }

    public void setOriginAccountId(Account originAccountId) {
        this.originAccountId = originAccountId;
    }

    public Account getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Account destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public User getPaymasterId() {
        return paymasterId;
    }

    public void setPaymasterId(User paymasterId) {
        this.paymasterId = paymasterId;
    }

    public User getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(User receiverId) {
        this.receiverId = receiverId;
    }
}
