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
import java.time.LocalDate;
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
                return accountList;
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are Accounts.");

        } else {
            return null;
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
            Optional<AccountHolder> optionalPrimaryAccountHolder = accountHolderRepository.findById(primaryOwnerId);
            if (optionalPrimaryAccountHolder.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Primary Owner does not exist");
            } else if (secondaryOwnerId != null) {
                Optional<AccountHolder> optionalSecondaryAccountHolder = accountHolderRepository.findById(secondaryOwnerId);
                if (optionalSecondaryAccountHolder.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Secondary Owner does not exist");
                } else {
                    if (dataValidation.validateAgeOfPrimaryOwner(optionalPrimaryAccountHolder.get())) {
                        studentCheckingAccount = studentCheckingAccountRepository.save(new StudentCheckingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), optionalSecondaryAccountHolder.get(), secretKey, LocalDate.now(), Status.ACTIVE, AccountType.STUDENT_CHECKING));
                        studentCheckingAccount.setAccountNumber(studentCheckingAccount.getEntityNumber() + studentCheckingAccount.getBranchNumber() + studentCheckingAccount.getId().toString());
                        return studentCheckingAccountRepository.save(studentCheckingAccount);

                    } else {
                        checkingAccount = checkingAccountRepository.save(new CheckingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), optionalSecondaryAccountHolder.get(), secretKey, LocalDate.now(), Status.ACTIVE, AccountType.CHECKING));
                        checkingAccount.setAccountNumber(checkingAccount.getEntityNumber() + checkingAccount.getBranchNumber() + checkingAccount.getId().toString());
                        return checkingAccountRepository.save(checkingAccount);
                    }

                }
            } else if (dataValidation.validateAgeOfPrimaryOwner(optionalPrimaryAccountHolder.get())) {
                studentCheckingAccount = studentCheckingAccountRepository.save(new StudentCheckingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null, secretKey, LocalDate.now(), Status.ACTIVE, AccountType.STUDENT_CHECKING));
                studentCheckingAccount.setAccountNumber(studentCheckingAccount.getEntityNumber() + studentCheckingAccount.getBranchNumber() + studentCheckingAccount.getId().toString());
                return studentCheckingAccountRepository.save(studentCheckingAccount);

            } else {
                checkingAccount = checkingAccountRepository.save(new CheckingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null, secretKey, LocalDate.now(), Status.ACTIVE, AccountType.CHECKING));
                checkingAccount.setAccountNumber(checkingAccount.getEntityNumber() + checkingAccount.getBranchNumber() + checkingAccount.getId().toString());
                return checkingAccountRepository.save(checkingAccount);

            }
        }
    }

    @Override
    public Account createSavingAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String secretKey, String minimumBalance, String interestRate) {
        SavingAccount savingAccount;

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
            Optional<AccountHolder> optionalPrimaryAccountHolder = accountHolderRepository.findById(primaryOwnerId);

            if (optionalPrimaryAccountHolder.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Primary Owner does not exist");

            } else if (secondaryOwnerId != null) {
                Optional<AccountHolder> optionalSecondaryAccountHolder = accountHolderRepository.findById(secondaryOwnerId);
                if (optionalSecondaryAccountHolder.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Secondary Owner does not exist");
                } else {
                    savingAccount = savingAccountRepository.save(new SavingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), optionalSecondaryAccountHolder.get(),
                            secretKey, LocalDate.now(), Status.ACTIVE, AccountType.SAVING));
                    savingAccount.setAccountNumber(savingAccount.getEntityNumber() + savingAccount.getBranchNumber() + savingAccount.getId().toString());
                    if (minimumBalance!=null){
                        savingAccount.setMinimumBalance(new BigDecimal(minimumBalance));
                    }else if(interestRate!=null){
                        savingAccount.setInterestRate(Float.valueOf(interestRate));
                    }
                    return savingAccountRepository.save(savingAccount);
                }
            } else {
                savingAccount = savingAccountRepository.save(new SavingAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null,
                        secretKey, LocalDate.now(), Status.ACTIVE, AccountType.SAVING));
                savingAccount.setAccountNumber(savingAccount.getEntityNumber() + savingAccount.getBranchNumber() + savingAccount.getId().toString());
                if (minimumBalance!=null){
                    savingAccount.setMinimumBalance(new BigDecimal(minimumBalance));
                }else if(interestRate!=null){
                    savingAccount.setInterestRate(Float.valueOf(interestRate));
                }
                return savingAccountRepository.save(savingAccount);
            }
        }
    }

    @Override
    public Account createCreditCardAccount(String entityNumber, String branchNumber, String amount, Long primaryOwnerId, Long secondaryOwnerId, String credit_limit, String interestRate) {
        CreditCardAccount creditCardAccount;

        if (entityNumber == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity number must be provided");
        } else if (branchNumber == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Branch number must be provided");
        } else if (amount == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Balance must be provided");
        } else if (primaryOwnerId == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Primary Owner must be provided");
        } else{
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
                            AccountType.CREDIT_CARD));
                    creditCardAccount.setAccountNumber(creditCardAccount.getEntityNumber() + creditCardAccount.getBranchNumber() + creditCardAccount.getId().toString());
                    if (credit_limit!=null){
                        try{
                            creditCardAccount.setCreditLimit(new BigDecimal(credit_limit));
                        }catch (RollbackException e){
                            throw e;
                        }

                    }else if(interestRate!=null){
                        creditCardAccount.setInterestRate(Float.valueOf(interestRate));
                    }

                    return creditCardAccountRepository.save(creditCardAccount);
                }
            } else {
                creditCardAccount = creditCardAccountRepository.save(new CreditCardAccount(entityNumber, branchNumber, balance, optionalPrimaryAccountHolder.get(), null,
                        AccountType.CREDIT_CARD));
                creditCardAccount.setAccountNumber(creditCardAccount.getEntityNumber() + creditCardAccount.getBranchNumber() + creditCardAccount.getId().toString());
                if (credit_limit!=null){
                    try{
                        creditCardAccount.setCreditLimit(new BigDecimal(credit_limit));
                    }catch (RollbackException e){
                        throw e;
                    }
                }else if(interestRate!=null){
                    creditCardAccount.setInterestRate(Float.valueOf(interestRate));
                }
                return creditCardAccountRepository.save(creditCardAccount);
            }
        }
    }
}