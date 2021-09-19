package com.deingun.bankingsystem.service.interfaces;

import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.security.CustomUserDetails;

import java.util.List;

public interface AccountService {

    List<Account> findAllAccounts(CustomUserDetails customUserDetails);

    String getAccountBalance(String accountNumber, CustomUserDetails customUserDetails);

    Account createCheckingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey);

    Account createSavingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId,
                                String secretKey, String minimumBalance, String interestRate);

    Account createCreditCardAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId,
                                String credit_limit, String interestRate);

    void updateBalance(String accountNumber, String amount);
}
