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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
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
     * @param customUserDetails CustomUserDetails
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
    /**
     * method to get account Balance of one account of the active user in the app
     *
     * @param accountNumber String
     * @param customUserDetails CustomUserDetails
     */
    @Override
    public String getAccountBalance(String accountNumber, CustomUserDetails customUserDetails) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());
        if (optionalAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account number provided does not exist");
        } else {
            if (optionalAccount.get().getSecondaryOwner() != null) {
                if (!optionalAccount.get().getPrimaryOwner().getUsername().equals(customUserDetails.getUsername())
                        && !optionalAccount.get().getSecondaryOwner().getUsername().equals(optionalUser.get().getUsername())) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account number provided does not correspond to the active user in the application");
                } else {
                    applyInterestRate(List.of(optionalAccount.get()));
                    return optionalAccount.get().getBalance().toString();
                }
            } else {
                if (!optionalAccount.get().getPrimaryOwner().getUsername().equals(optionalUser.get().getUsername())) {
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
     * @param entityNumber String
     * @param branchNumber String
     * @param amount BigDecimal
     * @param primaryOwnerId Long
     * @param secondaryOwnerId Long
     * @param secretKey String
     */

    @Override
    public Account createCheckingAccount(String entityNumber, String branchNumber, BigDecimal amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey) {

        CheckingAccount checkingAccount;
        StudentCheckingAccount studentCheckingAccount;

            Money balance = new Money(amount);
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
     * @param entityNumber String
     * @param branchNumber String
     * @param amount BigDecimal
     * @param primaryOwnerId Long
     * @param secondaryOwnerId Long
     * @param secretKey String
     * @param minimumBalance String
     * @param interestRate String
     */
    @Override
    public Account createSavingAccount(String entityNumber, String branchNumber, BigDecimal amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey, BigDecimal minimumBalance, Float interestRate) {
        SavingAccount savingAccount;


            Money balance = new Money(amount);
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
                        savingAccount.setMinimumBalance(minimumBalance);
                    } else if (interestRate != null) {
                        savingAccount.setInterestRate(interestRate);
                    }
                    return savingAccountRepository.save(savingAccount);
                }
            } else {
                savingAccount = savingAccountRepository.save(new SavingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null,
                        secretKey, LocalDate.now(), Status.ACTIVE, AccountType.SAVING,LocalDate.now()));
                savingAccount.setAccountNumber(savingAccount.getEntityNumber() + savingAccount.getBranchNumber() + savingAccount.getId().toString());
                if (minimumBalance != null) {
                    savingAccount.setMinimumBalance(minimumBalance);
                } else if (interestRate != null) {
                    savingAccount.setInterestRate(interestRate);
                }
                return savingAccountRepository.save(savingAccount);
            }

    }
    /**
     * method to create a new Credit Card account
     *
     * @param entityNumber String
     * @param branchNumber String
     * @param amount BigDecimal
     * @param primaryOwnerId Long
     * @param secondaryOwnerId Long
     * @param credit_limit String
     * @param interestRate String
     */
    @Override
    public Account createCreditCardAccount(String entityNumber, String branchNumber, BigDecimal amount, Long primaryOwnerId, Long secondaryOwnerId, BigDecimal credit_limit, Float interestRate) {
        CreditCardAccount creditCardAccount;


            Money balance = new Money(amount);
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
                        creditCardAccount.setCreditLimit(credit_limit);

                    } else if (interestRate != null) {
                        creditCardAccount.setInterestRate(interestRate);
                    }

                    return creditCardAccountRepository.save(creditCardAccount);
                }
            } else {
                creditCardAccount = creditCardAccountRepository.save(new CreditCardAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null,
                        AccountType.CREDIT_CARD,LocalDate.now()));
                creditCardAccount.setAccountNumber(creditCardAccount.getEntityNumber() + creditCardAccount.getBranchNumber() + creditCardAccount.getId().toString());
                if (credit_limit != null) {
                    creditCardAccount.setCreditLimit(credit_limit);
                } else if (interestRate != null) {
                    creditCardAccount.setInterestRate(interestRate);
                }
                return creditCardAccountRepository.save(creditCardAccount);
            }

    }

    /**
     * method to Update Balance of any account by Admin User
     *
     * @param accountNumber String
     * @param amount BigDecimal
     */
    @Override
    public void updateBalance(String accountNumber, BigDecimal amount) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account number provided does not exist");
        } else {

            if (DataValidation.validateAmount(amount)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The amount must be more than zero");
            } else {
                Account account = optionalAccount.get();
                account.setBalance(new Money(amount));
                accountRepository.save(account);
            }
        }
    }

    /**
     * method to apply interest Rate if applicable
     *
     * @param accountList List<Account>
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

    @Override
    public void updateStatus(String accountNumber, Status status) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (optionalAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account number provided does not exist");
        } else {

           try{
               if (optionalAccount.get() instanceof CheckingAccount) {
                   CheckingAccount checkingAccount = (CheckingAccount) optionalAccount.get();
                   checkingAccount.setStatus(status);
                   checkingAccountRepository.save(checkingAccount);
               } else if (optionalAccount.get() instanceof StudentCheckingAccount) {
                   StudentCheckingAccount studentCheckingAccount = (StudentCheckingAccount) optionalAccount.get();
                   studentCheckingAccount.setStatus(status);
                   studentCheckingAccountRepository.save(studentCheckingAccount);
               } else if (optionalAccount.get() instanceof SavingAccount) {
                   SavingAccount savingAccount = (SavingAccount) optionalAccount.get();
                   savingAccount.setStatus(status);
                   savingAccountRepository.save(savingAccount);
               }
           }catch (Exception e){
               throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Status provided is not correct. Must be ACTIVE or FROZEN");
           }


        }
    }

}