package com.deingun.bankingsystem.validations;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {

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
    public boolean validateMail(String email){
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

}
