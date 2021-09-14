package com.deingun.bankingsystem.service.impl;

import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.service.interfaces.UserService;
import com.deingun.bankingsystem.utils.Address;
import com.deingun.bankingsystem.validations.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    DataValidation dataValidation = new DataValidation();

    @Override
    public List<User> findAll() {

        List<User> userList = userRepository.findAll();
        if (userList.size() != 0) {
            return userList;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no users.");
    }

    @Override
    public AccountHolder findById(Long id) {
        return accountHolderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account holder with id " + id + " not found"));
    }

    @Override
    public List<AccountHolder> findAllAccountHolders() {
        List<AccountHolder> accountHolderList = accountHolderRepository.findAll();
        if (accountHolderList.size() != 0) {
            return accountHolderList;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no users.");
    }

    @Override
    public AccountHolder createAccountHolder(String username, String password, String name, String nif, LocalDate dateOfBirth, String street, String city, String country, Integer postalCode, String mailingAddress){
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exist");
        } else {
            String dataNotProvided = DataNotProvided(username, password, name, nif, dateOfBirth, street, city, country, postalCode);
            if (dataNotProvided != null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, dataNotProvided + " must be provided");
            } else {

                if (dataValidation.validateName(name)) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The name entered is invalid. It must contain at least name and surname");
                } else if (dataValidation.validatePassword(password)) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The password must be at least 6 characters");
                } else if (mailingAddress != null && dataValidation.validateMail(mailingAddress)) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The email entered is invalid.");
                } else {

                    if (mailingAddress == null) {
                        mailingAddress = "Email not provided";
                    }
                    Address address = new Address(street, city, country, postalCode);
                    AccountHolder accountHolder = new AccountHolder(username, passwordEncoder.encode(password), LocalDate.now(), name, nif, dateOfBirth, address, mailingAddress);
                    return accountHolderRepository.save(accountHolder);
                }
            }
        }
    }

    @Override
    public void updateAccountHolder(Long id, String username, String password, String name, String nif, LocalDate dateOfBirth, Address address, String mailingAddress) {

    }

    @Override
    public void deleteAccountHolder(Long id) {

    }

    /**
     * method to check if any data is not provided
     *
     * @return String
     */
    String DataNotProvided(String username, String password, String name, String nif, LocalDate dateOfBirth, String street, String city, String country, Integer postalCode) {
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