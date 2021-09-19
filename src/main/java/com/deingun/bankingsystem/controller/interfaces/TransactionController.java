package com.deingun.bankingsystem.controller.interfaces;

import com.deingun.bankingsystem.controller.dto.TransactionDTO;
import com.deingun.bankingsystem.enums.TransactionType;
import com.deingun.bankingsystem.model.Transaction;
import com.deingun.bankingsystem.security.CustomUserDetails;

public interface TransactionController {

    Transaction newTransaction(TransactionDTO transactionDTO, CustomUserDetails customUserDetails);
    String newThirdPartyTransaction(String HashedKey, TransactionDTO transactionDTO, CustomUserDetails customUserDetails);
}
