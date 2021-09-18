package com.deingun.bankingsystem.controller.impl;

import com.deingun.bankingsystem.controller.dto.TransactionDTO;
import com.deingun.bankingsystem.controller.interfaces.TransactionController;
import com.deingun.bankingsystem.model.Transaction;
import com.deingun.bankingsystem.service.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TransactionControllerImpl implements TransactionController {
    @Autowired
    TransactionService transactionService;


    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction newTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        return transactionService.newTransaction(transactionDTO.getOriginAccount(),
                transactionDTO.getDestinationAccount(), transactionDTO.getAmount());
    }
}
