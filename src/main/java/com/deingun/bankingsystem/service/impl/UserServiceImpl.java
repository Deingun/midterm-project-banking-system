package com.deingun.bankingsystem.service.impl;

import com.deingun.bankingsystem.enums.Role;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.repository.user.UserRepository;
import com.deingun.bankingsystem.security.SecurityConfiguration;
import com.deingun.bankingsystem.service.interfaces.UserService;
import com.deingun.bankingsystem.utils.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    SecurityConfiguration securityConfiguration = new SecurityConfiguration();


    @Autowired
    UserRepository userRepository;


    @Override
    public List<User> findAll() {

        List<User> userList = userRepository.findAll();
        if (userList.size() != 0) {
            return userList;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no users.");
    }

    @Override
    public User createAccountHolder(String username, String password, LocalDate passwordDate, String name, String nif, LocalDate dateOfBirth, Address address, String mailingAddress) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exist");
        } else {
            if (username == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Title must be provided");
            } else if (password == null) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Password must be provided");
            } else {
                User user = new AccountHolder(username,password,passwordDate,Role.ACCOUNTHOLDER,name,nif,dateOfBirth,address,mailingAddress);

                return userRepository.save(user);
            }
    }
}
}