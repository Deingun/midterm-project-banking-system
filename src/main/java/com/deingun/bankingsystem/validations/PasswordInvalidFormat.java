package com.deingun.bankingsystem.validations;

public class PasswordInvalidFormat extends Exception{

    public PasswordInvalidFormat() {
    }
    public PasswordInvalidFormat(String message) {
        super(message);
    }
}
