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
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepostory roleRepostory;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    User userTest1;
    User userTest2;
    User userTest3;
    User userTest4;
    User userTest5;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        Address addressTest = new Address("streetTest", "cityTest", "countryTest", 22222);
        userTest1 = new AccountHolder("accountHolderTest1", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest1", "11111111A", LocalDate.of(1980, 10, 5), addressTest, "test@gmail.com");
        userTest2 = new AccountHolder("accountHolderTest2", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest2", "22222222F", LocalDate.of(1990, 2, 15), addressTest, "test@gmail.com");
        userTest3 = new Admin("adminTest1", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest3");
        userTest4 = new ThirdParty("thirdPartyTest1", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest4", "abc123");
        userTest5 = new ThirdParty("thirdPartyTest2", passwordEncoder.encode("123456"), LocalDate.now(), "NameTest5", "abc123");
        userRepository.saveAll(List.of(userTest1, userTest2, userTest3, userTest4, userTest5));
        Role roleTest1 = new Role(Roles.ACCOUNTHOLDER, userTest1);
        Role roleTest2 = new Role(Roles.ACCOUNTHOLDER, userTest2);
        Role roleTest3 = new Role(Roles.ADMIN, userTest3);
        Role roleTest4 = new Role(Roles.THIRDPARTY, userTest4);
        Role roleTest5 = new Role(Roles.THIRDPARTY, userTest5);
        roleRepostory.saveAll(List.of(roleTest1, roleTest2, roleTest3, roleTest4, roleTest5));
        userTest1.setRoleSet(Set.of(roleTest1));
        userTest2.setRoleSet(Set.of(roleTest2));
        userTest3.setRoleSet(Set.of(roleTest3));
        userTest4.setRoleSet(Set.of(roleTest4));
        userTest5.setRoleSet(Set.of(roleTest5));
        userRepository.saveAll(List.of(userTest1, userTest2, userTest3, userTest4, userTest5));

    }

    @AfterEach
    void tearDown() {
        roleRepostory.deleteAll();
        userRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
        thirdPartyRepository.deleteAll();

    }

    @Test
    void findById_validId_isPresent() {
        Optional<User> optionalUser = userRepository.findById(userTest1.getId());
        assertTrue(optionalUser.isPresent());
        assertEquals("accountHolderTest1", optionalUser.get().getUsername());
        assertEquals(LocalDate.now(), optionalUser.get().getPasswordDate());
    }

    @Test
    void findById_invalidId_isEmpty() {
        Optional<User> optionalUser = userRepository.findById(99L);
        assertTrue(optionalUser.isEmpty());

    }

    @Test
    void findAll_noParams_UserList() {
        List<User> userList = userRepository.findAll();
        assertEquals(5, userList.size());
        assertEquals("accountHolderTest1", userList.get(0).getUsername());
        assertEquals("accountHolderTest2", userList.get(1).getUsername());
        assertEquals("adminTest1", userList.get(2).getUsername());
        assertEquals("thirdPartyTest1", userList.get(3).getUsername());
        assertEquals("thirdPartyTest2", userList.get(4).getUsername());
    }

}