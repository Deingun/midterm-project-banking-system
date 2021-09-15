package com.deingun.bankingsystem.service.interfaces;

import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.account.CheckingAccount;

import java.util.List;

public interface AccountService {

    List<CheckingAccount> findAllCheckingAccounts();

    Account createCheckingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey);
}
