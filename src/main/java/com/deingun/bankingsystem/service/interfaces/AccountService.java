package com.deingun.bankingsystem.service.interfaces;

import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.utils.Money;

import java.time.LocalDate;
import java.util.List;

public interface AccountService {

    List<CheckingAccount> findAllCheckingAccounts();

    CheckingAccount createCheckingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey);
}
