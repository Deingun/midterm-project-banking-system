package com.deingun.bankingsystem.service.impl;

import com.deingun.bankingsystem.enums.Role;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.repository.user.AccountHolderRepository;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.security.SecurityConfiguration;
import com.deingun.bankingsystem.service.interfaces.UserService;
import com.deingun.bankingsystem.utils.Address;
import com.deingun.bankingsystem.validations.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;


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
    public AccountHolder createAccountHolder(String username, String password, String name, String nif, LocalDate dateOfBirth, String street, String city,String country,int postalCode, String mailingAddress) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exist");
        } else {
            if (username == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username must be provided");
            } else if (password == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Password must be provided");
            }else if (name == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Name must be provided");
            }else if (nif == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Nif must be provided");
            }
            else {
                Address address = new Address(street,city,country,postalCode);
                AccountHolder accountHolder = new AccountHolder(username,passwordEncoder.encode(password),LocalDate.now(),name,nif,dateOfBirth,address,mailingAddress);
                return accountHolderRepository.save(accountHolder);
            }
    }
}

    @Override
    public void updateAccountHolder(Long id, String username, String password, String name, String nif, LocalDate dateOfBirth, Address address, String mailingAddress) {

    }

    @Override
    public void deleteAccountHolder(Long id) {

    }


}