package com.deingun.bankingsystem.service.interfaces;

import com.deingun.bankingsystem.model.Transaction;

public interface TransactionService {

    Transaction newTransaction(String originAccountNumber, String destinationAccountNumber, String amount);
}
