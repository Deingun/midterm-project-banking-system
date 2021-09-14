package com.deingun.bankingsystem.service.impl;

import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.repository.account.CheckingAccountRepository;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.service.interfaces.AccountService;
import com.deingun.bankingsystem.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Override
    public List<CheckingAccount> findAllCheckingAccounts() {
        List<CheckingAccount> checkingAccountList = checkingAccountRepository.findAll();
        if (checkingAccountList.size() != 0) {
            return checkingAccountList;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no Checking Accounts.");
    }

    @Override
    public CheckingAccount createCheckingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey) {

        CheckingAccount checkingAccount;
        if (entityNumber == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity number must be provided");
        } else if (branchNumber == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Branch number must be provided");
        } else if (amount == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Balance must be provided");
        } else if (primaryOwnerId == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Primary Owner must be provided");
        } else if (secretKey == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Secret Key must be provided");
        } else {
            Money balance = new Money(new BigDecimal(amount));
            Optional<AccountHolder> optionalAccountHolder = accountHolderRepository.findById(primaryOwnerId);
            if (optionalAccountHolder.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Primary Owner does not exist");
            } else if (secondaryOwnerId != null) {
                Optional<AccountHolder> optionalSecondaryAccountHolder = accountHolderRepository.findById(secondaryOwnerId);
                if (optionalSecondaryAccountHolder.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Secondary Owner does not exist");
                } else {

                    checkingAccount = checkingAccountRepository.save(new CheckingAccount(entityNumber, branchNumber, balance, optionalAccountHolder.get(), optionalSecondaryAccountHolder.get(), secretKey, LocalDate.now(), Status.ACTIVE));
                    checkingAccount.setAccountNumber(checkingAccount.getEntityNumber() + checkingAccount.getBranchNumber() + checkingAccount.getId().toString());
                    return checkingAccountRepository.save(checkingAccount);
                }
            } else

                checkingAccount = checkingAccountRepository.save(new CheckingAccount(entityNumber, branchNumber, balance, optionalAccountHolder.get(), null, secretKey, LocalDate.now(), Status.ACTIVE));
                checkingAccount.setAccountNumber(checkingAccount.getEntityNumber() + checkingAccount.getBranchNumber() + checkingAccount.getId().toString());
                return checkingAccountRepository.save(checkingAccount);
        }
    }
}
