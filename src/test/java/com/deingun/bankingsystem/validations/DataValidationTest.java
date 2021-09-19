package com.deingun.bankingsystem.validations;

import com.deingun.bankingsystem.model.user.AccountHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataValidationTest {

    DataValidation dataValidation;

    @Test
    void validateName_ValidName_False() {
        String name = "Pepito López";
        Boolean invalidName = DataValidation.validateName(name);
        assertFalse(invalidName);
    }

    @Test
    void validateName_InvalidName_True() {
        String name = "Pepito";
        Boolean invalidName = DataValidation.validateName(name);
        assertTrue(invalidName);
    }

    @Test
    void validateAmount_ValidAmount_False() {
        String amount = "100";
        Boolean invalidAmount = DataValidation.validateAmount(amount);
        assertFalse(invalidAmount);
    }

    @Test
    void validateAmount_InvalidAmount_True() {
        String amount = "-100";
        Boolean invalidAmount = DataValidation.validateAmount(amount);
        assertTrue(invalidAmount);
    }

    @Test
    void validateMail_ValidMail_False() {
        String mail = "pepitolopez@gmail.com";
        Boolean invalidMail = DataValidation.validateMail(mail);
        assertFalse(invalidMail);
    }

    @Test
    void validateName_InvalidMAil_True() {
        String mail = "pepitolópezgmail.com";
        Boolean invalidMail = DataValidation.validateMail(mail);
        assertTrue(invalidMail);
    }

    @Test
    void validatePassword_ValidPassword_False() {
        String password = "123456";
        Boolean invalidpassword = DataValidation.validatePassword(password);
        assertFalse(invalidpassword);
    }

    @Test
    void validatePassword_InvalidPassword_False() {
        String password = "12345";
        Boolean invalidpassword = DataValidation.validatePassword(password);
        assertTrue(invalidpassword);
    }

    @Test
    void validateAgeOfPrimaryOwner_LessThan24_True() {
        AccountHolder accountHolder = new AccountHolder("accountHolderTest1", "123456", LocalDate.now(), "NameTest1", "11111111A", LocalDate.of(2005, 10, 5), null, "test@gmail.com");
        Boolean lessThan24 = DataValidation.validateAgeOfPrimaryOwner(accountHolder);
        assertTrue(lessThan24);
    }

    @Test
    void validateAgeOfPrimaryOwner_LessThan24_False() {
        AccountHolder accountHolder = new AccountHolder("accountHolderTest1", "123456", LocalDate.now(), "NameTest1", "11111111A", LocalDate.of(1980, 10, 5), null, "test@gmail.com");
        Boolean lessThan24 = DataValidation.validateAgeOfPrimaryOwner(accountHolder);
        assertFalse(lessThan24);
    }

    @Test
    void dataNotProvided_AllDataProvided_ReturnNull() {
        String username = "Mario_Lopez";String password = "123456";
        String name = "Mario Lopez";String nif = "546416546Z";
        LocalDate dateOfBirth = LocalDate.of(1980, 10, 5);
        String street = "Calle Real";String city = " Madrid";
        String country = "Spain";Integer postalCode = 28016;
        String dataValidation = DataValidation.DataNotProvided(username,password,name,nif,dateOfBirth,street,city,country,postalCode);
        assertEquals(null,dataValidation);
    }

    @Test
    void dataNotProvided_NoAllDataProvided_ReturnNull() {
        String username = "Mario_Lopez";String password = "123456";
        String name = "Mario Lopez";String nif = null;
        LocalDate dateOfBirth = LocalDate.of(1980, 10, 5);
        String street = "Calle Real";String city = " Madrid";
        String country = "Spain";Integer postalCode = 28016;
        String dataValidation = DataValidation.DataNotProvided(username,password,name,nif,dateOfBirth,street,city,country,postalCode);
        assertEquals("Nif",dataValidation);

        String username1 = "Mario_Lopez";String password1 = "123456";
        String name1 = "Mario Lopez";String nif1 = "546416546Z";
        LocalDate dateOfBirth1 = null;
        String street1 = "Calle Real";String city1 = " Madrid";
        String country1 = "Spain";Integer postalCode1 = 28016;
        String dataValidation1 = DataValidation.DataNotProvided(username1,password1,name1,nif1,dateOfBirth1,street1,city1,country1,postalCode1);
        assertEquals("Date of birth",dataValidation1);
    }
}