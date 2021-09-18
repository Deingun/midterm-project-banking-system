package com.deingun.bankingsystem.service.interfaces;

import com.deingun.bankingsystem.enums.AccountType;
import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.security.CustomUserDetails;
import com.deingun.bankingsystem.utils.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AccountService {

    List<Account> findAllAccounts(CustomUserDetails customUserDetails);

    Account createCheckingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey);

    Account createSavingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId,
                                String secretKey, String minimumBalance, String interestRate);

    Account createCreditCardAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId,
                                String credit_limit, String interestRate);
}
