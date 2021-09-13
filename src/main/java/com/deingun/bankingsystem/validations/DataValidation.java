package com.deingun.bankingsystem.validations;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidation {

    /**
     * method to validate a correct name
     *
     * @param name Should "firstName lastName"
     * @throws NameInvalidFormat validation of the correct imputation of the name
     */
    public void validateName(String name) throws NameInvalidFormat {
        name = name.trim();
        StringTokenizer words = new StringTokenizer(name);
        if (words.countTokens() < 2) {
            throw new NameInvalidFormat("The name entered is invalid. It must contain at least name and surname");
        }
    }

    /**
     * method to validate if the entered mail is correct
     *
     * @param phoneNumber long phoneNumber
     * @throws PhoneInvalidFormat validation of the correct imputation of the phone number
     */
    public void validatePhoneNumber(long phoneNumber) throws PhoneInvalidFormat {
        String strLong = Long.toString(phoneNumber);
        if (strLong.length() > 15 || strLong.length() < 5) {
            throw new PhoneInvalidFormat("The phone number entered is invalid");
        }
    }

    /**
     * method to validate if the phone number is correct
     *
     * @param email String email
     * @throws EmailInvalidFormat validation of the correct imputation of the email
     */
    public void validateMail(String email) throws EmailInvalidFormat {
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        if (!mather.find()) {
            throw new EmailInvalidFormat("The email entered is invalid");
        }
    }

    /**
     * method to validate if the password is correct
     *
     * @param password String password
     * @throws PasswordInvalidFormat validation of the correct imputation of the email
     */
    public void validatePassword(String password) throws PasswordInvalidFormat {

        if (password.length() < 6){
            throw new PasswordInvalidFormat("The password must be at least 6 characters");
        }
    }

}
