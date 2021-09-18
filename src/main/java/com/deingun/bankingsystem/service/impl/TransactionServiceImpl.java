package com.deingun.bankingsystem.service.impl;

import com.deingun.bankingsystem.enums.AccountType;
import com.deingun.bankingsystem.model.Transaction;
import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.repository.TransactionRepository;
import com.deingun.bankingsystem.repository.account.AccountRepository;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.service.interfaces.TransactionService;
import com.deingun.bankingsystem.utils.Money;
import com.deingun.bankingsystem.validations.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final DataValidation dataValidation = new DataValidation();

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction newTransaction(String originAccountNumber, String destinationAccountNumber,String amount) {

        Optional<Account> optionalOriginAccount = accountRepository.findByAccountNumber(originAccountNumber);
        Optional<Account> optionalDestinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber);

        if(optionalOriginAccount.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The origin account provided does not exist");
        }else if(optionalDestinationAccount.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The destination account provided does not exist");
        }else if(validateBalance(originAccountNumber,amount)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The account does not have sufficient funds to complete the transaction.");
        } else if(validateBalanceWithPenaltyFee(originAccountNumber,amount)){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The account does not have sufficient funds to complete the transaction. \" " +
                    "The resulting balance would be less than the minimum allowed balance and there are not enough funds to charge the penalty fee.");
        }

        else{

            Optional<User> optionalPaymasterUser = userRepository.findById(optionalOriginAccount.get().getPrimaryOwner().getId());
            Optional<User> optionalReceiverUser = userRepository.findById(optionalDestinationAccount.get().getPrimaryOwner().getId());

            Transaction transaction = new Transaction(optionalOriginAccount.get(),optionalDestinationAccount.get(),
                    optionalPaymasterUser.get(), optionalReceiverUser.get(),new Money(new BigDecimal(amount)), LocalDateTime.now());

            BigDecimal newBalanceOriginAccount = optionalOriginAccount.get().getBalance().decreaseAmount(new Money(new BigDecimal(amount)));
            optionalOriginAccount.get().setBalance(new Money(newBalanceOriginAccount));

            BigDecimal newBalanceDestinationAccount = optionalDestinationAccount.get().getBalance().increaseAmount(new Money(new BigDecimal(amount)));
            optionalDestinationAccount.get().setBalance(new Money(newBalanceDestinationAccount));

            accountRepository.save(optionalOriginAccount.get());
            accountRepository.save(optionalDestinationAccount.get());
            return transactionRepository.save(transaction);

        }

    }

    /**
     * method to validate if there is enough balance in the account
     *
     * @param accountNumber, amount
     */
    public boolean validateBalance(String accountNumber, String amount) {
        Optional <Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        BigDecimal balance = optionalAccount.get().getBalance().getAmount();
        BigDecimal transferAmount = new BigDecimal(amount);
        int result = balance.compareTo(transferAmount);

        if (result!= 1) {
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
        Optional <Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);

        if(optionalAccount.get().getAccountType() == AccountType.CHECKING || optionalAccount.get().getAccountType() == AccountType.SAVING){
            BigDecimal newBalanceOriginAccount = optionalAccount.get().getBalance().decreaseAmount(new Money(new BigDecimal(amount)));
            BigDecimal penaltyFee = new BigDecimal("40");
            int result = newBalanceOriginAccount.compareTo(penaltyFee);
            if (result!= 1) {
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
    }
}
