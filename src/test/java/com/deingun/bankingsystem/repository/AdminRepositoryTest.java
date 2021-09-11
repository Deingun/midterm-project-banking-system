package com.deingun.bankingsystem.repository;

import com.deingun.bankingsystem.enums.Roles;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.Admin;
import com.deingun.bankingsystem.model.user.Role;
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
class AdminRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepostory roleRepostory;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    Admin admin1;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        Address addressTest = new Address("streetTest", "cityTest", "countryTest", 22222);

        admin1 = new Admin("adminTest1", passwordEncoder.encode("123456"), LocalDate.now(), "admin");
        adminRepository.save(admin1);
        Role roleTest3 = new Role(Roles.ADMIN, admin1);
        roleRepostory.save(roleTest3);
        admin1.setRoleSet(Set.of(roleTest3));
        adminRepository.save(admin1);
    }

    @AfterEach
    void tearDown() {
        roleRepostory.deleteAll();
        userRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Test
    void findByUserId_validId_isPresent() {
        Optional<Admin> optionalAdmin = adminRepository.findById(admin1.getId());
        assertTrue(optionalAdmin.isPresent());
    }

    @Test
    void findByUserId_invalidId_isEmpty() {
        Optional<Admin> optionalAdmin = adminRepository.findById(99L);
        assertTrue(optionalAdmin.isEmpty());

    }

    @Test
    void findAll_noParams_AccountHoldersList() {
        List<Admin> accountHolderList = adminRepository.findAll();
        assertEquals(1, accountHolderList.size());
        assertEquals("adminTest1", accountHolderList.get(0).getUsername());
    }
}