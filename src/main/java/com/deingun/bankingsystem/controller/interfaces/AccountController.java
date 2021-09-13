package com.deingun.bankingsystem.controller.interfaces;

import com.deingun.bankingsystem.controller.dto.CheckingAccountDTO;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.utils.Money;

import java.util.List;

public interface AccountController {

    List<CheckingAccount> findAllCheckingAccounts();

    CheckingAccount createCheckingAccount(CheckingAccountDTO checkingAccountDTO);
}
