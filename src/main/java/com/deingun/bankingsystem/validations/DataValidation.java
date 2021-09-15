package com.deingun.bankingsystem.validations;

import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataValidation {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    /**
     * method to validate a correct name
     *
     * @param name Should "firstName lastName"
     */
    public boolean validateName(String name) {
        name = name.trim();
        StringTokenizer words = new StringTokenizer(name);
        if (words.countTokens() < 2) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * method to validate if the phone number is correct
     *
     * @param email String email
     */
    public boolean validateMail(String email) {
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
    public boolean validatePassword(String password) {

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
    public boolean validateAgeOfPrimaryOwner(AccountHolder accountHolder) {
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
    public String DataNotProvided(String username, String password, String name, String nif, LocalDate dateOfBirth, String street, String city, String country, Integer postalCode) {
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
}
