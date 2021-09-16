package com.deingun.bankingsystem.controller.impl;

import com.deingun.bankingsystem.enums.AccountType;
import com.deingun.bankingsystem.enums.Status;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.Admin;
import com.deingun.bankingsystem.repository.account.CheckingAccountRepository;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import com.deingun.bankingsystem.repository.user.AdminRepository;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.utils.Address;
import com.deingun.bankingsystem.utils.Money;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountControllerImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    AccountHolder accountHolderTest1;
    AccountHolder accountHolderTest2;
    Admin adminTest1;
    CheckingAccount checkingAccountTest1;
    CheckingAccount checkingAccountTest2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        Address addressTest = new Address("streetTest", "cityTest", "countryTest", 22222);
        Money balance = new Money(new BigDecimal("1000"));
        accountHolderTest1 = new AccountHolder("accountHolderTest1", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest1", "11111111A", LocalDate.of(1980, 10, 5), addressTest, "test@gmail.com");
        accountHolderTest2 = new AccountHolder("accountHolderTest2", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest2", "22222222F", LocalDate.of(1990, 2, 15), addressTest, "test@gmail.com");
        accountHolderRepository.saveAll(List.of(accountHolderTest1, accountHolderTest2));
        adminTest1 = new Admin("adminTest1", passwordEncoder.encode("123456"), LocalDate.now(), "admin");
        adminRepository.save(adminTest1);

        checkingAccountTest1 = new CheckingAccount("0049", "1500", balance, accountHolderTest1, accountHolderTest2, "123abc",
                LocalDate.now(), Status.ACTIVE, AccountType.CHECKING);
        checkingAccountTest2 = new CheckingAccount("0049", "2020", balance, accountHolderTest2, accountHolderTest1, "123abc",
                LocalDate.now(), Status.ACTIVE,AccountType.CHECKING);

        checkingAccountRepository.saveAll(List.of(checkingAccountTest1, checkingAccountTest2));
        checkingAccountTest1.setAccountNumber(checkingAccountTest1.getEntityNumber() + checkingAccountTest1.getBranchNumber() + checkingAccountTest1.getId().toString());
        checkingAccountTest2.setAccountNumber(checkingAccountTest2.getEntityNumber() + checkingAccountTest2.getBranchNumber() + checkingAccountTest2.getId().toString());
        checkingAccountRepository.saveAll(List.of(checkingAccountTest1, checkingAccountTest2));
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        userRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Test
    void getAll_NoParams_AllChechingAccounts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/accounts").with(httpBasic("accountHolderTest1", "123456")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("0049"));
        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("1500"));
        assertTrue(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8).contains("123abc"));

    }

}