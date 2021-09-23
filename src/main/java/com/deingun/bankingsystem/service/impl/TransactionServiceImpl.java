package com.deingun.bankingsystem.service.impl;

import com.deingun.bankingsystem.enums.AccountType;
import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.enums.TransactionType;
import com.deingun.bankingsystem.model.Transaction;
import com.deingun.bankingsystem.model.account.*;
import com.deingun.bankingsystem.model.user.ThirdParty;
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

    /**
     * method for accountholeders to send money to other accounts.
     *
     * @param originAccountNumber, destinationAccountNumber, amount, customUserDetails
     */
    @Override
    public Transaction newTransaction(String originAccountNumber, String destinationAccountNumber, BigDecimal amount, CustomUserDetails customUserDetails) {

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
        }else if (!validateStatus(originAccountNumber)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The state account is frozen. Unable to make transactions ");
        }
        else {

            Optional<User> optionalPaymasterUser = userRepository.findById(optionalOriginAccount.get().getPrimaryOwner().getId());
            Optional<User> optionalReceiverUser = userRepository.findById(optionalDestinationAccount.get().getPrimaryOwner().getId());

            Transaction transaction = new Transaction(optionalOriginAccount.get(), optionalDestinationAccount.get(),
                    optionalPaymasterUser.get(), optionalReceiverUser.get(), new Money(amount), LocalDateTime.now());

            BigDecimal newBalanceOriginAccount = optionalOriginAccount.get().getBalance().decreaseAmount(new Money(amount));
            optionalOriginAccount.get().setBalance(new Money(newBalanceOriginAccount));

            BigDecimal newBalanceDestinationAccount = optionalDestinationAccount.get().getBalance().increaseAmount(new Money(amount));
            optionalDestinationAccount.get().setBalance(new Money(newBalanceDestinationAccount));

            accountRepository.save(optionalOriginAccount.get());
            accountRepository.save(optionalDestinationAccount.get());

            chargePenaltyFee(optionalOriginAccount.get().getAccountNumber(), newBalanceOriginAccount);

            return transactionRepository.save(transaction);
        }

    }
    /**
     * method for third-party users to receive and send money to other accounts.
     *
     * @param hashedKey, accountNumber, amount, secretKey, transactionType, customUserDetails
     */
    @Override
    public String newThirdPartyTransaction(String hashedKey, String accountNumber, BigDecimal amount, String secretKey, TransactionType transactionType, CustomUserDetails customUserDetails) {


        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());
        ThirdParty thirdParty = (ThirdParty) optionalUser.get();

        if (optionalAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The account provided does not exist");
        } else if (!thirdParty.getHashedKey().equals(hashedKey)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The hashed key provided is wrong");
        } else if (transactionType == TransactionType.DEBIT && validateBalance(accountNumber, amount)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The account does not have sufficient funds to complete the transaction.");
        } else if (transactionType == TransactionType.DEBIT && validateBalanceWithPenaltyFee(accountNumber, amount)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The account does not have sufficient funds to complete the transaction. \" " +
                    "The resulting balance would be less than the minimum allowed balance and there are not enough funds to charge the penalty fee.");
        } else if (optionalAccount.get() instanceof CreditCardAccount) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The account type provided in incorrect.");
        } else {

            String accountSecretKey = getSecretKey(accountNumber);

            if (!accountSecretKey.equals(secretKey)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The secretKey key provided is wrong");
            } else {

                Transaction transaction = new Transaction(thirdParty, optionalAccount.get(), new Money(amount), LocalDateTime.now());
                if (transactionType == TransactionType.CREDIT) {
                    BigDecimal newBalanceAccount = optionalAccount.get().getBalance().increaseAmount(new Money(amount));
                    optionalAccount.get().setBalance(new Money(newBalanceAccount));
                    accountRepository.save(optionalAccount.get());
                    transactionRepository.save(transaction);
                    return amount + " USD credit made to the account " + accountNumber + ".";

                } else {
                    BigDecimal newBalanceAccount = optionalAccount.get().getBalance().decreaseAmount(new Money(amount));
                    optionalAccount.get().setBalance(new Money(newBalanceAccount));
                    accountRepository.save(optionalAccount.get());
                    chargePenaltyFee(optionalAccount.get().getAccountNumber(), newBalanceAccount);
                    transactionRepository.save(transaction);
                    return amount + " USD debit made to the account " + accountNumber + ".";
                }
            }
        }
    }

    /**
     * method to validate if there is enough balance in the account
     *
     * @param accountNumber, amount
     */
    public boolean validateBalance(String accountNumber, BigDecimal amount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        BigDecimal balance = optionalAccount.get().getBalance().getAmount();
        int result = balance.compareTo(amount);

        return result != 1;
    }

    /**
     * method to validate if the status account is Active
     *
     * @param accountNumber, amount
     */
    public boolean validateStatus(String accountNumber) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalAccount.get() instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) optionalAccount.get();
                if (checkingAccount.getStatus() == Status.ACTIVE){
                    return true;
            }else {
                    return false;
                }
        }else if (optionalAccount.get() instanceof StudentCheckingAccount) {
            StudentCheckingAccount studentCheckingAccount = (StudentCheckingAccount) optionalAccount.get();
            if (studentCheckingAccount.getStatus() == Status.ACTIVE){
                return true;
            }else {
                return false;
            }
        }else if (optionalAccount.get() instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) optionalAccount.get();
            if (savingAccount.getStatus() == Status.ACTIVE){
                return true;
            }else {
                return false;
            }
        }else{
            return true;
        }
    }

    /**
     * method to validate if there is enough balance in the account to charge the penalty Fee
     *
     * @param accountNumber, amount
     */
    public boolean validateBalanceWithPenaltyFee(String accountNumber, BigDecimal amount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        BigDecimal balance = optionalAccount.get().getBalance().getAmount();
        BigDecimal penaltyFee = optionalAccount.get().getPENALTYFEE();

        if (optionalAccount.get().getAccountType() == AccountType.CHECKING || optionalAccount.get().getAccountType() == AccountType.SAVING) {
            int result = balance.compareTo(amount.add(penaltyFee));
            return result < 0;
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
            return !optionalAccount.get().getPrimaryOwner().getId().equals(optionalUser.get().getId()) && !optionalAccount.get().getSecondaryOwner().getId().equals(optionalUser.get().getId());
        } else {
            return !optionalAccount.get().getPrimaryOwner().getId().equals(optionalUser.get().getId());
        }
    }

    /**
     * method to charge the penalty Fee
     *
     * @param accountNumber, amount
     */
    public void chargePenaltyFee(String accountNumber, BigDecimal newBalanceOriginAccount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalAccount.get() instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) optionalAccount.get();
            BigDecimal minimumBalance = checkingAccount.getMinimumBalance();
            if (newBalanceOriginAccount.compareTo(minimumBalance) == -1) {
                BigDecimal newBalance = optionalAccount.get().getBalance().decreaseAmount(checkingAccount.getPENALTYFEE());
                optionalAccount.get().setBalance(new Money(newBalance));
                accountRepository.save(optionalAccount.get());
            }

        }
        if (optionalAccount.get() instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) optionalAccount.get();
            BigDecimal minimumBalance = savingAccount.getMinimumBalance();
            if (newBalanceOriginAccount.compareTo(minimumBalance) == -1) {
                BigDecimal newBalance = optionalAccount.get().getBalance().decreaseAmount(savingAccount.getPENALTYFEE());
                optionalAccount.get().setBalance(new Money(newBalance));
                accountRepository.save(optionalAccount.get());
            }
        }
    }


    /**
     * method to get the secrete key
     *
     * @param accountNumber
     */
    public String getSecretKey(String accountNumber) {

        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);

        if (optionalAccount.get() instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) optionalAccount.get();
            return checkingAccount.getSecretKey();
        } else if (optionalAccount.get() instanceof StudentCheckingAccount) {
            StudentCheckingAccount studentCheckingAccount = (StudentCheckingAccount) optionalAccount.get();
            return studentCheckingAccount.getSecretKey();
        } else {
            SavingAccount savingAccount = (SavingAccount) optionalAccount.get();
            return savingAccount.getSecretKey();
        }
    }

    /**
     * method to detect pattern that indicate fraud for transactions made in 24 hours total to more
     * than 150% of the customers highest daily total transactions in any other 24 hour period.
     *
     */
    public boolean maxTransactionAllowed() {
        return false;

    }

}