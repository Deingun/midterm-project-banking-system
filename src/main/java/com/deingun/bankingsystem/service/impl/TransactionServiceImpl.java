package com.deingun.bankingsystem.service.impl;

import com.deingun.bankingsystem.enums.AccountType;
import com.deingun.bankingsystem.model.Transaction;
import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.account.SavingAccount;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.repository.TransactionRepository;
import com.deingun.bankingsystem.repository.account.AccountRepository;
import com.deingun.bankingsystem.repository.account.CheckingAccountRepository;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.security.CustomUserDetails;
import com.deingun.bankingsystem.service.interfaces.TransactionService;
import com.deingun.bankingsystem.utils.Money;
import com.deingun.bankingsystem.validations.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final DataValidation dataValidation = new DataValidation();

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction newTransaction(String originAccountNumber, String destinationAccountNumber, String amount, CustomUserDetails customUserDetails) {

        if (originAccountNumber == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Origin account number must be provided");
        } else if (destinationAccountNumber == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Destination account number must be provided");
        } else if (amount == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount must be provided");
        }else if (DataValidation.validateAmount(amount)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The amount must be more than zero");
        } else{
            Optional<Account> optionalOriginAccount = accountRepository.findByAccountNumber(originAccountNumber);
            Optional<Account> optionalDestinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber);
            Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());

            if (optionalOriginAccount.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The origin account provided does not exist");
            } else if (optionalDestinationAccount.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The destination account provided does not exist");
            } else if (validateOwner(originAccountNumber, customUserDetails)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The transaction cannot be made since you are not the primary or secondary owner of the selected account.");
            } else if (validateBalance(originAccountNumber, amount)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The account does not have sufficient funds to complete the transaction.");
            } else if (validateBalanceWithPenaltyFee(originAccountNumber, amount)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The account does not have sufficient funds to complete the transaction. \" " +
                        "The resulting balance would be less than the minimum allowed balance and there are not enough funds to charge the penalty fee.");
            } else {

                Optional<User> optionalPaymasterUser = userRepository.findById(optionalOriginAccount.get().getPrimaryOwner().getId());
                Optional<User> optionalReceiverUser = userRepository.findById(optionalDestinationAccount.get().getPrimaryOwner().getId());

                Transaction transaction = new Transaction(optionalOriginAccount.get(), optionalDestinationAccount.get(),
                        optionalPaymasterUser.get(), optionalReceiverUser.get(), new Money(new BigDecimal(amount)), LocalDateTime.now());

                BigDecimal newBalanceOriginAccount = optionalOriginAccount.get().getBalance().decreaseAmount(new Money(new BigDecimal(amount)));
                optionalOriginAccount.get().setBalance(new Money(newBalanceOriginAccount));

                BigDecimal newBalanceDestinationAccount = optionalDestinationAccount.get().getBalance().increaseAmount(new Money(new BigDecimal(amount)));
                optionalDestinationAccount.get().setBalance(new Money(newBalanceDestinationAccount));

                accountRepository.save(optionalOriginAccount.get());
                accountRepository.save(optionalDestinationAccount.get());

                chargePenaltyFee(optionalOriginAccount.get().getAccountNumber(),newBalanceOriginAccount);

                return transactionRepository.save(transaction);
            }
        }

    }

    /**
     * method to validate if there is enough balance in the account
     *
     * @param accountNumber, amount
     */
    public boolean validateBalance(String accountNumber, String amount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        BigDecimal balance = optionalAccount.get().getBalance().getAmount();
        BigDecimal transactionAmount = new BigDecimal(amount);
        int result = balance.compareTo(transactionAmount);

        if (result != 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * method to validate if there is enough balance in the account to charge the penalty Fee
     *
     * @param accountNumber, amount
     */
    public boolean validateBalanceWithPenaltyFee(String accountNumber, String amount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        BigDecimal balance = optionalAccount.get().getBalance().getAmount();
        BigDecimal transactionAmount = new BigDecimal(amount);
        BigDecimal penaltyFee = optionalAccount.get().getPENALTYFEE();

        if (optionalAccount.get().getAccountType() == AccountType.CHECKING || optionalAccount.get().getAccountType() == AccountType.SAVING) {
            int result = balance.compareTo(transactionAmount.add(penaltyFee));
            if (result == -1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * method to validate if the account provided corresponds to the active user in the application
     *
     * @param accountNumber, customUserDetails
     */
    public boolean validateOwner(String accountNumber, CustomUserDetails customUserDetails) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());

        if (optionalAccount.get().getSecondaryOwner() != null) {
            if (optionalAccount.get().getPrimaryOwner().getId() != optionalUser.get().getId() && optionalAccount.get().getSecondaryOwner().getId() != optionalUser.get().getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            if (optionalAccount.get().getPrimaryOwner().getId() != optionalUser.get().getId()) {
                return true;
            } else {
                return false;
            }
        }
    }
    /**
     * method to charge the penalty Fee
     *
     * @param accountNumber, amount
     */
    public void chargePenaltyFee (String accountNumber, BigDecimal newBalanceOriginAccount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if(optionalAccount.get() instanceof CheckingAccount){
            CheckingAccount checkingAccount = (CheckingAccount) optionalAccount.get();
            BigDecimal minimumBalance = checkingAccount.getMinimumBalance();
            if (newBalanceOriginAccount.compareTo(minimumBalance)==-1){
                BigDecimal newBalance = optionalAccount.get().getBalance().decreaseAmount(checkingAccount.getPENALTYFEE());
                optionalAccount.get().setBalance(new Money(newBalance));
                accountRepository.save(optionalAccount.get());
            }

        }
        if(optionalAccount.get() instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) optionalAccount.get();
            BigDecimal minimumBalance = savingAccount.getMinimumBalance();
            if(newBalanceOriginAccount.compareTo(minimumBalance)==-1){
                BigDecimal newBalance = optionalAccount.get().getBalance().decreaseAmount(savingAccount.getPENALTYFEE());
                optionalAccount.get().setBalance(new Money(newBalance));
                accountRepository.save(optionalAccount.get());
            }
        }
    }
}
