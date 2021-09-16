package com.deingun.bankingsystem.repository;

import com.deingun.bankingsystem.enums.AccountType;
import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.Transaction;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.repository.account.CheckingAccountRepository;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.utils.Address;
import com.deingun.bankingsystem.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionRepositoryTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    AccountHolder accountHolderTest1;
    AccountHolder accountHolderTest2;
    CheckingAccount checkingAccountTest1;
    CheckingAccount checkingAccountTest2;

    Transaction transactionTest1;
    Transaction transactionTest2;
    Transaction transactionTest3;


    @BeforeEach
    void setUp() {

        Address addressTest = new Address("streetTest", "cityTest", "countryTest", 22222);
        Money balance = new Money(new BigDecimal("1000"));

        accountHolderTest1 = new AccountHolder("accountHolderTest1", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest1", "11111111A", LocalDate.of(1980, 10, 5), addressTest, "test@gmail.com");
        accountHolderTest2 = new AccountHolder("accountHolderTest2", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest2", "22222222F", LocalDate.of(1990, 2, 15), addressTest, "test@gmail.com");
        accountHolderRepository.saveAll(List.of(accountHolderTest1, accountHolderTest2));


        checkingAccountTest1 = new CheckingAccount("0049", "1500", balance, accountHolderTest1, null, "123abc",
                LocalDate.now(), Status.ACTIVE, AccountType.CHECKING);
        checkingAccountTest2 = new CheckingAccount("0049", "2020", balance, accountHolderTest2, null, "123abc",
                LocalDate.now(), Status.ACTIVE,AccountType.CHECKING);

        checkingAccountRepository.saveAll(List.of(checkingAccountTest1, checkingAccountTest2));
        checkingAccountTest1.setAccountNumber(checkingAccountTest1.getEntityNumber() + checkingAccountTest1.getBranchNumber() + checkingAccountTest1.getId().toString());
        checkingAccountTest2.setAccountNumber(checkingAccountTest2.getEntityNumber() + checkingAccountTest2.getBranchNumber() + checkingAccountTest2.getId().toString());
        checkingAccountRepository.saveAll(List.of(checkingAccountTest1, checkingAccountTest2));

        User user1 = userRepository.findByUsername(accountHolderTest1.getUsername()).get();
        User user2 = userRepository.findByUsername(accountHolderTest2.getUsername()).get();
        Money amount1 = new Money(new BigDecimal("10"));
        transactionTest1 = new Transaction(checkingAccountTest1, checkingAccountTest2, user1, user2, amount1, LocalDateTime.now());
        Money amount2 = new Money(new BigDecimal("50"));
        transactionTest2 = new Transaction(checkingAccountTest1, checkingAccountTest2, user2, user1, amount2, LocalDateTime.now());
        transactionRepository.saveAll(List.of(transactionTest1, transactionTest2));
        Money amount3 = new Money(new BigDecimal("20"));
        transactionTest3 = new Transaction(checkingAccountTest1, checkingAccountTest2, user1, user2, amount2, LocalDateTime.now());
        transactionRepository.saveAll(List.of(transactionTest1, transactionTest2, transactionTest3));

    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        userRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void findById_validId_isPresent() {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionTest1.getId());
        assertTrue(optionalTransaction.isPresent());
    }

    @Test
    void findById_invalidId_isEmpty() {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(-1L);
        assertTrue(optionalTransaction.isEmpty());
    }

    @Test
    void findAll_noParams_TransactionList() {
        List<Transaction> transactionList = transactionRepository.findAll();
        assertEquals(3, transactionList.size());
    }

    @Test
    void findAll_Username_TransactionList() {
        List<Transaction> transactionList = transactionRepository.findAllByPaymasterUsername(accountHolderTest1.getUsername());
        assertEquals(2, transactionList.size());
        assertEquals(new BigDecimal("50.00"), transactionList.get(1).getAmount().getAmount());
        assertEquals("USD", transactionList.get(1).getAmount().getCurrency().toString());
        assertEquals("accountHolderTest2", transactionList.get(1).getReceiver().getUsername());
    }
}