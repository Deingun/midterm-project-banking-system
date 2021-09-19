package com.deingun.bankingsystem.service.impl;

import com.deingun.bankingsystem.enums.AccountType;
import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.Transaction;
import com.deingun.bankingsystem.model.account.*;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.repository.TransactionRepository;
import com.deingun.bankingsystem.repository.account.*;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.security.CustomUserDetails;
import com.deingun.bankingsystem.utils.Address;
import com.deingun.bankingsystem.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceImplTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CreditCardAccountRepository creditCardAccountRepository;

    @Autowired
    SavingAccountRepository savingAccountRepository;

    @Autowired
    StudentCheckingAccountRepository studentCheckingAccountRepository;

    @Autowired
    TransactionServiceImpl transactionServiceImpl;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    AccountHolder accountHolderTest1;
    AccountHolder accountHolderTest2;
    AccountHolder accountHolderTest3;
    CheckingAccount checkingAccountTest1;
    CheckingAccount checkingAccountTest2;
    CheckingAccount checkingAccountTest3;
    CreditCardAccount creditCardAccountTest1;
    CreditCardAccount creditCardAccountTest2;
    SavingAccount savingAccountTest1;
    SavingAccount savingAccountTest2;
    SavingAccount savingAccountTest3;
    StudentCheckingAccount studentCheckingAccountTest1;
    StudentCheckingAccount studentCheckingAccountTest2;
    CustomUserDetails customUserDetails;


    @BeforeEach
    void setUp() {

        Address addressTest = new Address("streetTest", "cityTest", "countryTest", 22222);
        Money balance = new Money(new BigDecimal("1000"));
        BigDecimal minimumBalance = new BigDecimal("1000").setScale(3, RoundingMode.HALF_EVEN);

        accountHolderTest1 = new AccountHolder("accountHolderTest1", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest1", "11111111A", LocalDate.of(1980, 10, 5), addressTest, "test@gmail.com");
        accountHolderTest2 = new AccountHolder("accountHolderTest2", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest2", "22222222F", LocalDate.of(1990, 2, 15), addressTest, "test@gmail.com");
        accountHolderTest3 = new AccountHolder("accountHolderTest3", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest3", "33333333F", LocalDate.of(2000, 2, 15), addressTest, "test@gmail.com");
        accountHolderRepository.saveAll(List.of(accountHolderTest1, accountHolderTest2, accountHolderTest3));


        checkingAccountTest1 = new CheckingAccount("0049", "1500", balance, accountHolderTest1, accountHolderTest2, "123abc",
                LocalDate.now(), Status.ACTIVE, AccountType.CHECKING);
        checkingAccountTest2 = new CheckingAccount("0049", "2020", balance, accountHolderTest2, accountHolderTest1, "123abc",
                LocalDate.now(), Status.ACTIVE, AccountType.CHECKING);
        checkingAccountTest3 = new CheckingAccount("0049", "2020", new Money(new BigDecimal("200")), accountHolderTest1,null, "123abc",
                LocalDate.now(), Status.ACTIVE, AccountType.CHECKING);


        checkingAccountRepository.saveAll(List.of(checkingAccountTest1, checkingAccountTest2,checkingAccountTest3));
        checkingAccountTest1.setAccountNumber(checkingAccountTest1.getEntityNumber() + checkingAccountTest1.getBranchNumber() + checkingAccountTest1.getId().toString());
        checkingAccountTest2.setAccountNumber(checkingAccountTest2.getEntityNumber() + checkingAccountTest2.getBranchNumber() + checkingAccountTest2.getId().toString());
        checkingAccountTest3.setAccountNumber(checkingAccountTest3.getEntityNumber() + checkingAccountTest3.getBranchNumber() + checkingAccountTest3.getId().toString());
        checkingAccountRepository.saveAll(List.of(checkingAccountTest1, checkingAccountTest2, checkingAccountTest3));

        creditCardAccountTest1 = new CreditCardAccount("0049", "1500", balance, accountHolderTest1, accountHolderTest2, AccountType.CREDIT_CARD);
        creditCardAccountTest2 = new CreditCardAccount("0049", "2020", balance, accountHolderTest2, accountHolderTest1, new BigDecimal("500"), 0.15F, AccountType.CREDIT_CARD);


        creditCardAccountRepository.saveAll(List.of(creditCardAccountTest1, creditCardAccountTest2));
        creditCardAccountTest1.setAccountNumber(creditCardAccountTest1.getEntityNumber() + creditCardAccountTest1.getBranchNumber() + creditCardAccountTest1.getId().toString());
        creditCardAccountTest2.setAccountNumber(creditCardAccountTest2.getEntityNumber() + creditCardAccountTest2.getBranchNumber() + creditCardAccountTest2.getId().toString());
        creditCardAccountRepository.saveAll(List.of(creditCardAccountTest1, creditCardAccountTest2));

        savingAccountTest1 = new SavingAccount("0049", "1500", balance, accountHolderTest1, accountHolderTest2, "123abc", minimumBalance, LocalDate.now(), Status.ACTIVE, 0.0050F, AccountType.SAVING);
        savingAccountTest2 = new SavingAccount("0049", "1500", balance, accountHolderTest2, accountHolderTest1, "123abc", minimumBalance, LocalDate.now(), Status.ACTIVE, AccountType.SAVING);
        savingAccountTest3 = new SavingAccount("0049", "1500", new Money(new BigDecimal("200")), accountHolderTest2, accountHolderTest1, "123abc", minimumBalance, LocalDate.now(), Status.ACTIVE, AccountType.SAVING);

        savingAccountRepository.saveAll(List.of(savingAccountTest1, savingAccountTest2,savingAccountTest3));
        savingAccountTest1.setAccountNumber(savingAccountTest1.getEntityNumber() + savingAccountTest1.getBranchNumber() + savingAccountTest1.getId().toString());
        savingAccountTest2.setAccountNumber(savingAccountTest2.getEntityNumber() + savingAccountTest2.getBranchNumber() + savingAccountTest2.getId().toString());
        savingAccountTest3.setAccountNumber(savingAccountTest3.getEntityNumber() + savingAccountTest3.getBranchNumber() + savingAccountTest3.getId().toString());
        savingAccountRepository.saveAll(List.of(savingAccountTest1, savingAccountTest2,savingAccountTest3));

        studentCheckingAccountTest1 = new StudentCheckingAccount("0049", "1500", balance, accountHolderTest1, accountHolderTest2, "123abc",
                LocalDate.now(), Status.ACTIVE, AccountType.STUDENT_CHECKING);
        studentCheckingAccountTest2 = new StudentCheckingAccount("0049", "2020", balance, accountHolderTest2, accountHolderTest1, "123abc",
                LocalDate.now(), Status.ACTIVE, AccountType.STUDENT_CHECKING);

        studentCheckingAccountRepository.saveAll(List.of(studentCheckingAccountTest1, studentCheckingAccountTest2));
        studentCheckingAccountTest1.setAccountNumber(studentCheckingAccountTest1.getEntityNumber() + studentCheckingAccountTest1.getBranchNumber() + studentCheckingAccountTest1.getId().toString());
        studentCheckingAccountTest2.setAccountNumber(studentCheckingAccountTest2.getEntityNumber() + studentCheckingAccountTest2.getBranchNumber() + studentCheckingAccountTest2.getId().toString());
        studentCheckingAccountRepository.saveAll(List.of(studentCheckingAccountTest1, studentCheckingAccountTest2));
    }

    @AfterEach
    void tearDown() {

        transactionRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        creditCardAccountRepository.deleteAll();
        savingAccountRepository.deleteAll();
        studentCheckingAccountRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        accountHolderRepository.deleteAll();
        customUserDetails = null;
    }

    @Test
    void newTransaction_Valid_newTransaction() {

        Optional<Account> optionalOriginAccount = accountRepository.findByAccountNumber(checkingAccountTest1.getAccountNumber());
        Optional<Account> optionalDestinationAccount = accountRepository.findByAccountNumber(checkingAccountTest2.getAccountNumber());
        customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername("accountHolderTest1");

        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());
        transactionServiceImpl.newTransaction(optionalOriginAccount.get().getAccountNumber(), optionalDestinationAccount.get().getAccountNumber(),
                "300", customUserDetails);
        accountRepository.flush();

        Optional<Account> optionalOriginAccountResult = accountRepository.findByAccountNumber(checkingAccountTest1.getAccountNumber());
        Optional<Account> optionalDestinationAccountResult = accountRepository.findByAccountNumber(checkingAccountTest2.getAccountNumber());
        assertEquals(optionalOriginAccount.get().getPrimaryOwner().getUsername(), optionalUser.get().getUsername());
        assertEquals(new BigDecimal("700").setScale(2, RoundingMode.HALF_EVEN), optionalOriginAccountResult.get().getBalance().getAmount());
        assertEquals(new BigDecimal("1300").setScale(2, RoundingMode.HALF_EVEN), optionalDestinationAccountResult.get().getBalance().getAmount());
    }

    @Test
    void newTransaction_InvalidAmountMoreThanBalance_newTransaction() {

        Optional<Account> optionalOriginAccount = accountRepository.findByAccountNumber(checkingAccountTest1.getAccountNumber());
        Optional<Account> optionalDestinationAccount = accountRepository.findByAccountNumber(checkingAccountTest2.getAccountNumber());
        customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(optionalOriginAccount.get().getPrimaryOwner().getUsername());
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            transactionServiceImpl.newTransaction(optionalOriginAccount.get().getAccountNumber(), optionalDestinationAccount.get().getAccountNumber(),
                    "2000", customUserDetails);
        });
    }

    @Test
    void newTransaction_InvalidAmountLessThanZero_newTransaction() {

        Optional<Account> optionalOriginAccount = accountRepository.findByAccountNumber(checkingAccountTest1.getAccountNumber());
        Optional<Account> optionalDestinationAccount = accountRepository.findByAccountNumber(checkingAccountTest2.getAccountNumber());
        customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(optionalOriginAccount.get().getPrimaryOwner().getUsername());
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            transactionServiceImpl.newTransaction(optionalOriginAccount.get().getAccountNumber(), optionalDestinationAccount.get().getAccountNumber(),
                    "-100", customUserDetails);
        });
    }

    @Test
    void newTransaction_InvalidOriginAccount_newTransaction() {

        Optional<Account> optionalOriginAccount = accountRepository.findByAccountNumber(checkingAccountTest1.getAccountNumber());
        Optional<Account> optionalDestinationAccount = accountRepository.findByAccountNumber(checkingAccountTest2.getAccountNumber());
        customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(optionalOriginAccount.get().getPrimaryOwner().getUsername());
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            transactionServiceImpl.newTransaction("9999999", optionalDestinationAccount.get().getAccountNumber(),
                    "700", customUserDetails);
        });
    }

    @Test
    void newTransaction_InvalidDestinationAccount_newTransaction() {

        Optional<Account> optionalOriginAccount = accountRepository.findByAccountNumber(checkingAccountTest1.getAccountNumber());
        Optional<Account> optionalDestinationAccount = accountRepository.findByAccountNumber(checkingAccountTest2.getAccountNumber());
        customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(optionalOriginAccount.get().getPrimaryOwner().getUsername());
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            transactionServiceImpl.newTransaction(optionalOriginAccount.get().getAccountNumber(), "999999999999",
                    "700", customUserDetails);
        });
    }

    @Test
    void newTransaction_InvalidUserDetails_newTransaction() {

        Optional<Account> optionalOriginAccount = accountRepository.findByAccountNumber(checkingAccountTest1.getAccountNumber());
        Optional<Account> optionalDestinationAccount = accountRepository.findByAccountNumber(checkingAccountTest2.getAccountNumber());
        customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(accountHolderTest3.getUsername());
        Optional<User> optionalUser = userRepository.findByUsername(customUserDetails.getUsername());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            transactionServiceImpl.newTransaction(optionalOriginAccount.get().getAccountNumber(), "999999999999",
                    "700", customUserDetails);
        });
    }

    @Test
    void validateBalance_EnoughtBalanceInAccount_ReturnFalse() {
        assertFalse(transactionServiceImpl.validateBalance(checkingAccountTest1.getAccountNumber(),"500"));
    }

    @Test
    void validateBalance_InsufficientBalanceInAccount_ReturnTrue() {
        assertTrue(transactionServiceImpl.validateBalance(checkingAccountTest1.getAccountNumber(),"2000"));
    }

    @Test
    void validateBalanceWithPenaltyFee_ApplyPenalty_UpdateAccountBalance() {
        assertTrue(transactionServiceImpl.validateBalanceWithPenaltyFee(checkingAccountTest3.getAccountNumber(),"200"));
    }

    @Test
    void validateBalanceWithPenaltyFee_NotApplyPenalty_UpdateAccountBalance() {
        assertFalse(transactionServiceImpl.validateBalanceWithPenaltyFee(checkingAccountTest1.getAccountNumber(),"200"));
    }

    @Test
    void validateOwner_ValidOwner_ReturnFalse() {
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(accountHolderTest1.getUsername());
        assertFalse(transactionServiceImpl.validateOwner(checkingAccountTest1.getAccountNumber(),customUserDetails));
    }

    @Test
    void validateOwner_InValidOwner_ReturnTrue() {
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(accountHolderTest2.getUsername());
        assertTrue(transactionServiceImpl.validateOwner(checkingAccountTest3.getAccountNumber(),customUserDetails));
    }

    @Test
    void chargePenaltyFee_CheckingAccountAndBalanceLessThanMinimumBalance_ApplyPenaltyFee() {
        transactionServiceImpl.chargePenaltyFee(checkingAccountTest3.getAccountNumber(),new BigDecimal("200"));
        accountRepository.flush();
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(checkingAccountTest3.getAccountNumber());
        assertEquals(new BigDecimal("160").setScale(2, RoundingMode.HALF_EVEN),optionalAccount.get().getBalance().getAmount());
    }

    @Test
    void chargePenaltyFee_SavingAccountAndBalanceLessThanMinimumBalance_ApplyPenaltyFee() {
        transactionServiceImpl.chargePenaltyFee(savingAccountTest3.getAccountNumber(),new BigDecimal("200"));
        savingAccountRepository.flush();
        Optional<Account> optionalAccount = savingAccountRepository.findByAccountNumber(savingAccountTest3.getAccountNumber());
        assertEquals(new BigDecimal("160").setScale(2, RoundingMode.HALF_EVEN),optionalAccount.get().getBalance().getAmount());
    }

    @Test
    void chargePenaltyFee_StudentAccount_NotApplyPenaltyFee() {
        transactionServiceImpl.chargePenaltyFee(studentCheckingAccountTest1.getAccountNumber(),new BigDecimal("200"));
        accountRepository.flush();
        Optional<Account> optionalAccount = studentCheckingAccountRepository.findByAccountNumber(studentCheckingAccountTest1.getAccountNumber());
        assertEquals(new BigDecimal("1000").setScale(2, RoundingMode.HALF_EVEN),optionalAccount.get().getBalance().getAmount());
    }

    @Test
    void getSecretKey() {
        assertEquals("123abc", transactionServiceImpl.getSecretKey(checkingAccountTest1.getAccountNumber()));
    }
}