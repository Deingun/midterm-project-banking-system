package com.deingun.bankingsystem.controller.interfaces;

import com.deingun.bankingsystem.controller.dto.TransactionDTO;
import com.deingun.bankingsystem.model.Transaction;

public interface TransactionController {

    Transaction newTransaction(TransactionDTO transactionDTO);
}
