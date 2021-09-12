package com.deingun.bankingsystem.repository.user;

import com.deingun.bankingsystem.enums.Roles;
import com.deingun.bankingsystem.model.user.Admin;
import com.deingun.bankingsystem.model.user.Role;
import com.deingun.bankingsystem.repository.user.AdminRepository;
import com.deingun.bankingsystem.repository.user.RoleRepostory;
import com.deingun.bankingsystem.repository.user.UserRepository;
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
    private PasswordEncoder passwordEncoder;

    Admin adminTest1;


    @BeforeEach
    void setUp() {

        adminTest1 = new Admin("adminTest1", passwordEncoder.encode("123456"), LocalDate.now(),Set.of(new Role(Roles.ADMIN)), "admin");
        adminRepository.save(adminTest1);

    }

    @AfterEach
    void tearDown() {
        roleRepostory.deleteAll();
        userRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Test
    void findByUserId_validId_isPresent() {
        Optional<Admin> optionalAdmin = adminRepository.findById(adminTest1.getId());
        assertTrue(optionalAdmin.isPresent());
    }

    @Test
    void findByUserId_invalidId_isEmpty() {
        Optional<Admin> optionalAdmin = adminRepository.findById(-1L);
        assertTrue(optionalAdmin.isEmpty());

    }

    @Test
    void findAll_noParams_AccountHoldersList() {
        List<Admin> accountHolderList = adminRepository.findAll();
        assertEquals(1, accountHolderList.size());
        assertEquals("adminTest1", accountHolderList.get(0).getUsername());
    }

    @Test
    void findByName_validName_isPresent() {
        Optional<Admin> optionalAdmin = adminRepository.findByName("admin");
        assertTrue(optionalAdmin.isPresent());
        assertEquals("adminTest1",optionalAdmin.get().getUsername());
    }

    @Test
    void findByName_invalidName_isEmpty() {
        Optional<Admin> optionalAdmin = adminRepository.findByName("invalidName");
        assertTrue(optionalAdmin.isEmpty());
    }
}