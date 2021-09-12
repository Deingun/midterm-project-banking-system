package com.deingun.bankingsystem.repository.user;

import com.deingun.bankingsystem.enums.Roles;
import com.deingun.bankingsystem.model.user.Role;
import com.deingun.bankingsystem.model.user.ThirdParty;
import com.deingun.bankingsystem.repository.user.RoleRepostory;
import com.deingun.bankingsystem.repository.user.ThirdPartyRepository;
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
class ThirdPartyRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepostory roleRepostory;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    ThirdParty thirdPartyTest1;
    ThirdParty thirdPartyTest2;

    @BeforeEach
    void setUp() {

        thirdPartyTest1 = new ThirdParty("thirdPartyTest1", passwordEncoder.encode("123456"), LocalDate.now(),Set.of(new Role(Roles.THIRDPARTY)), "nameTest1", "123abc");
        thirdPartyTest2 = new ThirdParty("thirdPartyTest2", passwordEncoder.encode("123456"), LocalDate.now(),Set.of(new Role(Roles.THIRDPARTY)), "nameTest2", "123abc");
        thirdPartyRepository.saveAll(List.of(thirdPartyTest1, thirdPartyTest2));

    }

    @AfterEach
    void tearDown() {
        roleRepostory.deleteAll();
        userRepository.deleteAll();
        thirdPartyRepository.deleteAll();
    }

    @Test
    void findByUserId_validId_isPresent() {
        Optional<ThirdParty> optionalThirdParty = thirdPartyRepository.findById(thirdPartyTest1.getId());
        assertTrue(optionalThirdParty.isPresent());
    }

    @Test
    void findByUserId_invalidId_isEmpty() {
        Optional<ThirdParty> optionalThirdParty = thirdPartyRepository.findById(-1L);
        assertTrue(optionalThirdParty.isEmpty());

    }

    @Test
    void findAll_noParams_AccountHoldersList() {
        List<ThirdParty> thirdPartyList = thirdPartyRepository.findAll();
        assertEquals(2, thirdPartyList.size());
        assertEquals("thirdPartyTest1", thirdPartyList.get(0).getUsername());
        assertEquals("thirdPartyTest2", thirdPartyList.get(1).getUsername());
    }

    @Test
    void findByName_validName_isPresent() {
        Optional<ThirdParty> optionalThirdParty = thirdPartyRepository.findByName("nameTest1");
        assertTrue(optionalThirdParty.isPresent());
        assertEquals("thirdPartyTest1", optionalThirdParty.get().getUsername());
    }

    @Test
    void findByName_invalidName_isEmpty() {
        Optional<ThirdParty> optionalThirdParty = thirdPartyRepository.findByName("invalidName");
        assertTrue(optionalThirdParty.isEmpty());
    }
}