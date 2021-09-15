package com.deingun.bankingsystem.controller.interfaces;

import com.deingun.bankingsystem.controller.dto.CheckingAccountDTO;
import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.account.CheckingAccount;

import java.util.List;

public interface AccountController {

    List<CheckingAccount> findAllCheckingAccounts();

    Account createCheckingAccount(CheckingAccountDTO checkingAccountDTO);
}
