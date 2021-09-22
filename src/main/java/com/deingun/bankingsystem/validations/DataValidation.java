package com.deingun.bankingsystem.validations;

import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.repository.account.AccountRepository;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import com.deingun.bankingsystem.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataValidation {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    static
    AccountRepository accountRepository;

    /**
     * method to validate a correct name
     *
     * @param name Should "firstName lastName"
     */
    public static boolean validateName(String name) {
        name = name.trim();
        StringTokenizer words = new StringTokenizer(name);
        if (words.countTokens() < 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * method to validate a correct amount
     *
     * @param amount Should be more than 0
     */
    public static boolean validateAmount(BigDecimal amount) {
        if (amount.signum()>0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * method to validate if the phone number is correct
     *
     * @param email String email
     */
    public static boolean validateMail(String email) {
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        if (!mather.find()) {
            return true;
        }
        return false;
    }

    /**
     * method to validate if the password is correct
     *
     * @param password String password
     */
    public static boolean validatePassword(String password) {

        if (password.length() < 6) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * method to validate if the PrimaryOwner is less than 24
     *
     * @param accountHolder
     */
    public static boolean validateAgeOfPrimaryOwner(AccountHolder accountHolder) {
        LocalDate dateForStudentAccount = LocalDate.now().minusYears(24L);
        if (accountHolder.getDateOfBirth().compareTo(dateForStudentAccount) < 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * method to check if any data is not provided
     *
     * @return String
     */
    public static String DataNotProvided(String username, String password, String name, String nif, LocalDate dateOfBirth, String street, String city, String country, Integer postalCode) {
        if (username == null) {
            return "Username";
        } else if (password == null) {
            return "Password";
        } else if (name == null) {
            return "Name";
        } else if (nif == null) {
            return "Nif";
        } else if (dateOfBirth == null) {
            return "Date of birth";
        } else if (street == null) {
            return "Street";
        } else if (city == null) {
            return "City";
        } else if (country == null) {
            return "Country";
        } else if (postalCode == null) {
            return "Postal code";
        }
        return null;
    }

    public static String DataNotProvided(String username, String password, String name, String hashedKey) {
        if (username == null) {
            return "Username";
        } else if (password == null) {
            return "Password";
        } else if (name == null) {
            return "Name";
        } else if (hashedKey == null) {
            return "Hashed Key";
        }
        return null;
    }
}
