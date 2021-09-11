package com.deingun.bankingsystem.repository.account;

import com.deingun.bankingsystem.enums.Roles;
import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.Role;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import com.deingun.bankingsystem.repository.user.RoleRepostory;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.utils.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
class CheckingAccountRepositoryTest {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    RoleRepostory roleRepostory;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    AccountHolder accountHolderTest1;
    AccountHolder accountHolderTest2;
    CheckingAccount checkingAccountTest1;
    CheckingAccount checkingAccountTest2;


    @BeforeEach
    void setUp() {

        Address addressTest = new Address("streetTest", "cityTest", "countryTest", 22222);
        accountHolderTest1 = new AccountHolder("accountHolderTest1", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest1", "11111111A", LocalDate.of(1980, 10, 5), addressTest, "test@gmail.com");
        accountHolderTest2 = new AccountHolder("accountHolderTest2", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest2", "22222222F", LocalDate.of(1990, 2, 15), addressTest, "test@gmail.com");
        accountHolderRepository.saveAll(List.of(accountHolderTest1, accountHolderTest2));
        Role roleTest1 = new Role(Roles.ACCOUNTHOLDER,accountHolderTest1);
        Role roleTest2 = new Role(Roles.ACCOUNTHOLDER,accountHolderTest2);
        roleRepostory.saveAll(List.of(roleTest1,roleTest2));
        accountHolderTest1.setRoleSet(Set.of(roleTest1));
        accountHolderTest2.setRoleSet(Set.of(roleTest2));
        accountHolderRepository.saveAll(List.of(accountHolderTest1, accountHolderTest2));


        checkingAccountTest1 = new CheckingAccount("0049","1500",new BigDecimal("1000"),accountHolderTest1,accountHolderTest2,"123abc",
                LocalDate.now(), Status.ACTIVE);
        checkingAccountTest2 = new CheckingAccount("0049","2020",new BigDecimal("1000"),accountHolderTest2,accountHolderTest1,"123abc",
                LocalDate.now(), Status.ACTIVE);

        checkingAccountRepository.saveAll(List.of(checkingAccountTest1,checkingAccountTest2));
        checkingAccountTest1.setAccountNumber(checkingAccountTest1.getEntityNumber()+checkingAccountTest1.getBranchNumber()+checkingAccountTest1.getId().toString());
        checkingAccountTest2.setAccountNumber(checkingAccountTest2.getEntityNumber()+checkingAccountTest2.getBranchNumber()+checkingAccountTest2.getId().toString());
        checkingAccountRepository.saveAll(List.of(checkingAccountTest1,checkingAccountTest2));
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        roleRepostory.deleteAll();
        userRepository.deleteAll();
        accountHolderRepository.deleteAll();

    }

    @Test
    void findById_validId_isPresent() {
        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountRepository.findById(checkingAccountTest1.getId());
        assertTrue(optionalCheckingAccount.isPresent());
    }

    @Test
    void findById_invalidId_isEmpty() {
        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountRepository.findById(-1L);
        assertTrue(optionalCheckingAccount.isEmpty());
    }

    @Test
    void findByPrimaryOwnerId_validId_isPresent() {
        List<CheckingAccount> checkingAccountList = checkingAccountRepository.findByPrimaryOwnerId(accountHolderTest1.getId());
        assertEquals(1, checkingAccountList.size());
        Assertions.assertThat(checkingAccountList.get(0).getBalance())
                .isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    void findAll_noParams_AccountList() {
        List<CheckingAccount>checkingAccountList = checkingAccountRepository.findAll();
        assertEquals(2, checkingAccountList.size());
    }
}