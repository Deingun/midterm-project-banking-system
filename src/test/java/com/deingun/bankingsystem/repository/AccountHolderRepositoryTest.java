package com.deingun.bankingsystem.repository;

import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.Admin;
import com.deingun.bankingsystem.model.user.ThirdParty;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.utils.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountHolderRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User userTest1;
    User userTest2;
    User userTest3;
    User userTest4;
    User userTest5;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @BeforeEach
    void setUp() {
        Address addressTest = new Address("streetTest", "cityTest", "countryTest", 22222);
        userTest1 = new AccountHolder("accountHolderTest1", "123456", LocalDate.now(), "NameTest1", "11111111A", LocalDate.of(1980, 10, 5), addressTest, "test@gmail.com");
        userTest2 = new AccountHolder("accountHolderTest2", "123456", LocalDate.now(), "NameTest2", "22222222F", LocalDate.of(1990, 2, 15), addressTest, "test@gmail.com");
        userTest3 = new Admin("adminTest1", "123456", LocalDate.now(), "NameTest3");
        userTest4 = new ThirdParty("thirdPartyTest1", "123456", LocalDate.now(), "NameTest4", "abc123");
        userTest5 = new ThirdParty("thirdPartyTest2", "123456", LocalDate.now(), "NameTest5", "abc123");
        userRepository.saveAll(List.of(userTest1, userTest2, userTest3, userTest4, userTest5));
    }

    @AfterEach
    void tearDown() {
         userRepository.deleteAll();
    }

//    @Test
//    void findById_validId_isPresent() {
//        Optional<AccountHolder> optionalAccountHolder = accountHolderRepository.findById(userTest1.getId()instanceof AccountHolder);
//        assertTrue(optionalAccountHolder.isPresent());
//        assertEquals("accountHolderTest1", optionalUser.get().getUsername());
//        assertEquals(LocalDate.now(), optionalUser.get().getPasswordDate());
//    }

    @Test
    void findById_invalidId_isEmpty() {
        Optional<User> optionalUser = userRepository.findById(99L);
        assertTrue(optionalUser.isEmpty());

    }

    @Test
    void findByUsername() {
    }

    @Test
    void findAll() {
    }
}