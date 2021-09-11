package com.deingun.bankingsystem.controller.interfaces;

import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.utils.Address;

import java.time.LocalDate;
import java.util.List;

public interface UserController {

    List<User> findAll();

    User createAccountHolder(String username, String password, LocalDate passwordDate, String name, String nif, LocalDate dateOfBirth, Address address, String mailingAddress);
}
