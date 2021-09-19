package com.deingun.bankingsystem.service.interfaces;

import com.deingun.bankingsystem.model.Transaction;
import com.deingun.bankingsystem.security.CustomUserDetails;

public interface TransactionService {

    Transaction newTransaction(String originAccountNumber, String destinationAccountNumber, String amount, CustomUserDetails customUserDetails);
}
