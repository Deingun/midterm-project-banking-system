package com.deingun.bankingsystem.repository;

import com.deingun.bankingsystem.enums.Roles;
import com.deingun.bankingsystem.model.user.*;
import com.deingun.bankingsystem.utils.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
class AccountHolderRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepostory roleRepostory;

    @Autowired
    AccountHolderRepository accountHolderRepository;


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    AccountHolder accountHolder1;
    AccountHolder accountHolder2;
    Admin admin;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        Address addressTest = new Address("streetTest", "cityTest", "countryTest", 22222);
        accountHolder1 = new AccountHolder("accountHolderTest1", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest1", "11111111A", LocalDate.of(1980, 10, 5), addressTest, "test@gmail.com");
        accountHolder2 = new AccountHolder("accountHolderTest2", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest2", "22222222F", LocalDate.of(1990, 2, 15), addressTest, "test@gmail.com");
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        Role roleTest1 = new Role(Roles.ACCOUNTHOLDER,accountHolder1);
        Role roleTest2 = new Role(Roles.ACCOUNTHOLDER,accountHolder2);
        roleRepostory.saveAll(List.of(roleTest1,roleTest2));
        accountHolder1.setRoleSet(Set.of(roleTest1));
        accountHolder2.setRoleSet(Set.of(roleTest2));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));

    }

    @AfterEach
    void tearDown() {
        roleRepostory.deleteAll();
        userRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void findByUserId_validId_isPresent() {
        Optional<AccountHolder> optionalAccountHolder = accountHolderRepository.findById(accountHolder1.getId());
        assertTrue(optionalAccountHolder.isPresent());
    }

    @Test
    void findByUserId_invalidId_isEmpty() {
        Optional<AccountHolder> optionalUser = accountHolderRepository.findById(99L);
        assertTrue(optionalUser.isEmpty());

    }


    @Test
    void findAll_noParams_AccountHoldersList() {
        List<AccountHolder> accountHolderList = accountHolderRepository.findAll();
        assertEquals(2, accountHolderList.size());
        assertEquals("accountHolderTest1", accountHolderList.get(0).getUsername());
        assertEquals("accountHolderTest2", accountHolderList.get(1).getUsername());
    }
}