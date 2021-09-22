package com.deingun.bankingsystem.service.impl;

import com.deingun.bankingsystem.enums.AccountType;
import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.exceptions.GlobalExceptionHandler;
import com.deingun.bankingsystem.model.account.*;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.repository.account.*;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.security.CustomUserDetails;
import com.deingun.bankingsystem.service.interfaces.AccountService;
import com.deingun.bankingsystem.utils.Money;
import com.deingun.bankingsystem.validations.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.RollbackException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final DataValidation dataValidation = new DataValidation();
    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    StudentCheckingAccountRepository studentCheckingAccountRepository;

    @Autowired
    SavingAccountRepository savingAccountRepository;

    @Autowired
    CreditCardAccountRepository creditCardAccountRepository;

    /**
     * method to create a new find all accounts of the active user in the application
     *
     * @param customUserDetails
     */
    @Override
    public List<Account> findAllAccounts(CustomUserDetails customUserDetails) {
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());
        if (optionalUser.isPresent()) {
            List<Account> accountList = accountRepository.findByPrimaryOwnerIdOrSecondaryOwnerId(optionalUser.get().getId(), optionalUser.get().getId());
            if (accountList.size() != 0) {
                applyInterestRate(accountList);
                return accountList;
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are Accounts.");

        } else {
            return null;
        }
    }

    @Override
    public String getAccountBalance(String accountNumber, CustomUserDetails customUserDetails) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());
        if (optionalAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account number provided does not exist");
        } else {
            if (optionalAccount.get().getSecondaryOwner() != null) {
                if (optionalAccount.get().getPrimaryOwner().getUsername() != customUserDetails.getUsername()
                        && optionalAccount.get().getSecondaryOwner().getUsername() != optionalUser.get().getUsername()) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account number provided does not correspond to the active user in the application");
                } else {
                    applyInterestRate(List.of(optionalAccount.get()));
                    return optionalAccount.get().getBalance().toString();
                }
            } else {
                if (optionalAccount.get().getPrimaryOwner().getUsername() != optionalUser.get().getUsername()) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account number provided does not correspond to the active user in the application");
                } else {
                    applyInterestRate(List.of(optionalAccount.get()));
                    return optionalAccount.get().getBalance().toString();
                }
            }

        }

    }

    /**
     * method to create a new Checking account, if the primaryOwner is less than 24, a StudentChecking account it will be created otherwise a regular Checking Account it will be created.
     *
     * @param entityNumber, branchNumber, amount, primaryOwnerId,  secondaryOwnerId, secretKey
     */

    @Override
    public Account createCheckingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey) {

        CheckingAccount checkingAccount;
        StudentCheckingAccount studentCheckingAccount;

            Money balance = new Money(new BigDecimal(amount));
            Optional<AccountHolder> optionalPrimaryAccountHolder = accountHolderRepository.findById(primaryOwnerId);
            if (optionalPrimaryAccountHolder.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Primary Owner does not exist");
            } else if (secondaryOwnerId != null) {
                Optional<AccountHolder> optionalSecondaryAccountHolder = accountHolderRepository.findById(secondaryOwnerId);
                if (optionalSecondaryAccountHolder.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Secondary Owner does not exist");
                } else {
                    if (DataValidation.validateAgeOfPrimaryOwner(optionalPrimaryAccountHolder.get())) {
                        studentCheckingAccount = studentCheckingAccountRepository.save(new StudentCheckingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), optionalSecondaryAccountHolder.get(), secretKey, LocalDate.now(), Status.ACTIVE, AccountType.STUDENT_CHECKING));
                        studentCheckingAccount.setAccountNumber(studentCheckingAccount.getEntityNumber() + studentCheckingAccount.getBranchNumber() + studentCheckingAccount.getId().toString());
                        return studentCheckingAccountRepository.save(studentCheckingAccount);

                    } else {
                        checkingAccount = checkingAccountRepository.save(new CheckingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), optionalSecondaryAccountHolder.get(), secretKey, LocalDate.now(), Status.ACTIVE, AccountType.CHECKING));
                        checkingAccount.setAccountNumber(checkingAccount.getEntityNumber() + checkingAccount.getBranchNumber() + checkingAccount.getId().toString());
                        return checkingAccountRepository.save(checkingAccount);
                    }

                }
            } else if (DataValidation.validateAgeOfPrimaryOwner(optionalPrimaryAccountHolder.get())) {
                studentCheckingAccount = studentCheckingAccountRepository.save(new StudentCheckingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null, secretKey, LocalDate.now(), Status.ACTIVE, AccountType.STUDENT_CHECKING));
                studentCheckingAccount.setAccountNumber(studentCheckingAccount.getEntityNumber() + studentCheckingAccount.getBranchNumber() + studentCheckingAccount.getId().toString());
                return studentCheckingAccountRepository.save(studentCheckingAccount);

            } else {
                checkingAccount = checkingAccountRepository.save(new CheckingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null, secretKey, LocalDate.now(), Status.ACTIVE, AccountType.CHECKING));
                checkingAccount.setAccountNumber(checkingAccount.getEntityNumber() + checkingAccount.getBranchNumber() + checkingAccount.getId().toString());
                return checkingAccountRepository.save(checkingAccount);

            }

    }

    /**
     * method to create a new Saving account
     *
     * @param entityNumber, branchNumber, amount, primaryOwnerId, secondaryOwnerId, secretKey, minimumBalance, interestRate
     */
    @Override
    public Account createSavingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey, String minimumBalance, String interestRate) {
        SavingAccount savingAccount;


            Money balance = new Money(new BigDecimal(amount));
            Optional<AccountHolder> optionalPrimaryAccountHolder = accountHolderRepository.findById(primaryOwnerId);

            if (optionalPrimaryAccountHolder.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Primary Owner does not exist");

            } else if (secondaryOwnerId != null) {
                Optional<AccountHolder> optionalSecondaryAccountHolder = accountHolderRepository.findById(secondaryOwnerId);
                if (optionalSecondaryAccountHolder.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Secondary Owner does not exist");
                } else {
                    savingAccount = savingAccountRepository.save(new SavingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), optionalSecondaryAccountHolder.get(),
                            secretKey, LocalDate.now(), Status.ACTIVE, AccountType.SAVING,LocalDate.now()));
                    savingAccount.setAccountNumber(savingAccount.getEntityNumber() + savingAccount.getBranchNumber() + savingAccount.getId().toString());
                    if (minimumBalance != null) {
                        savingAccount.setMinimumBalance(new BigDecimal(minimumBalance));
                    } else if (interestRate != null) {
                        savingAccount.setInterestRate(Float.valueOf(interestRate));
                    }
                    return savingAccountRepository.save(savingAccount);
                }
            } else {
                savingAccount = savingAccountRepository.save(new SavingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null,
                        secretKey, LocalDate.now(), Status.ACTIVE, AccountType.SAVING,LocalDate.now()));
                savingAccount.setAccountNumber(savingAccount.getEntityNumber() + savingAccount.getBranchNumber() + savingAccount.getId().toString());
                if (minimumBalance != null) {
                    savingAccount.setMinimumBalance(new BigDecimal(minimumBalance));
                } else if (interestRate != null) {
                    savingAccount.setInterestRate(Float.valueOf(interestRate));
                }
                return savingAccountRepository.save(savingAccount);
            }

    }
    /**
     * method to create a new Credit Card account
     *
     * @param entityNumber, branchNumber, amount, primaryOwnerId, secondaryOwnerId, credit_limit, interestRate
     */
    @Override
    public Account createCreditCardAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String credit_limit, String interestRate) {
        CreditCardAccount creditCardAccount;


            Money balance = new Money(new BigDecimal(amount));
            Optional<AccountHolder> optionalPrimaryAccountHolder = accountHolderRepository.findById(primaryOwnerId);

            if (optionalPrimaryAccountHolder.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Primary Owner does not exist");

            } else if (secondaryOwnerId != null) {
                Optional<AccountHolder> optionalSecondaryAccountHolder = accountHolderRepository.findById(secondaryOwnerId);
                if (optionalSecondaryAccountHolder.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Secondary Owner does not exist");
                } else {

                    creditCardAccount = creditCardAccountRepository.save(new CreditCardAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), optionalSecondaryAccountHolder.get(),
                            AccountType.CREDIT_CARD,LocalDate.now()));
                    creditCardAccount.setAccountNumber(creditCardAccount.getEntityNumber() + creditCardAccount.getBranchNumber() + creditCardAccount.getId().toString());
                    if (credit_limit != null) {
                        try {
                            creditCardAccount.setCreditLimit(new BigDecimal(credit_limit));
                        } catch (RollbackException e) {
                            throw e;
                        }

                    } else if (interestRate != null) {
                        creditCardAccount.setInterestRate(Float.valueOf(interestRate));
                    }

                    return creditCardAccountRepository.save(creditCardAccount);
                }
            } else {
                creditCardAccount = creditCardAccountRepository.save(new CreditCardAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null,
                        AccountType.CREDIT_CARD,LocalDate.now()));
                creditCardAccount.setAccountNumber(creditCardAccount.getEntityNumber() + creditCardAccount.getBranchNumber() + creditCardAccount.getId().toString());
                if (credit_limit != null) {
                    try {
                        creditCardAccount.setCreditLimit(new BigDecimal(credit_limit));
                    } catch (RollbackException e) {
                        throw e;
                    }
                } else if (interestRate != null) {
                    creditCardAccount.setInterestRate(Float.valueOf(interestRate));
                }
                return creditCardAccountRepository.save(creditCardAccount);
            }

    }

    /**
     * method to Update Balance of any account by Admin User
     *
     * @param accountNumber, amount
     */
    @Override
    public void updateBalance(String accountNumber, String amount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account number provided does not exist");
        } else {

            if (DataValidation.validateAmount(amount)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The amount must be more than zero");
            } else {
                Account account = optionalAccount.get();
                account.setBalance(new Money(new BigDecimal(amount)));
                accountRepository.save(account);
            }
        }
    }

    /**
     * method to apply interest Rate if applicable
     *
     * @param accountList
     */
    @Override
    public void applyInterestRate(List<Account> accountList) {

        for (Account account: accountList) {
            if(account.getAccountType().equals(AccountType.SAVING)){
                SavingAccount savingAccount = (SavingAccount) account;
                Period period = Period.between(savingAccount.getLastInterestRateDate(),LocalDate.now());
                if(period.getYears()>0){
                    BigDecimal interestAmount = (savingAccount.getBalance().getAmount())
                            .multiply(BigDecimal.valueOf(savingAccount.getInterestRate()))
                            .multiply(new BigDecimal(period.getYears()));
                    BigDecimal newBalance = savingAccount.getBalance().increaseAmount(interestAmount);
                    savingAccount.setBalance(new Money(newBalance));
                    savingAccount.setLastInterestRateDate(LocalDate.now());
                    savingAccountRepository.save(savingAccount);
                }
            }
            if(account.getAccountType().equals(AccountType.CREDIT_CARD)){
                CreditCardAccount creditCardAccount = (CreditCardAccount) account;
                Period period = Period.between(creditCardAccount.getLastInterestRateDate(),LocalDate.now());
                if(period.getMonths()>0){

                    BigDecimal interestAmount = (creditCardAccount.getBalance().getAmount())
                            .multiply(BigDecimal.valueOf(creditCardAccount.getInterestRate())).divide(new BigDecimal(12),2,RoundingMode.HALF_EVEN)
                            .multiply(new BigDecimal(period.getMonths()).add(new BigDecimal(period.getYears()*12)));
                    BigDecimal newBalance = creditCardAccount.getBalance().increaseAmount(interestAmount);
                    creditCardAccount.setBalance(new Money(newBalance));
                    creditCardAccount.setLastInterestRateDate(LocalDate.now());
                    creditCardAccountRepository.save(creditCardAccount);
                }
            }
        }
    }

}