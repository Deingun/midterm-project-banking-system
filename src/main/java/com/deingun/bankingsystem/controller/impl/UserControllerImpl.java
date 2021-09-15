package com.deingun.bankingsystem.controller.impl;

import com.deingun.bankingsystem.controller.dto.AccountHolderDTO;
import com.deingun.bankingsystem.controller.interfaces.UserController;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.service.interfaces.UserService;
import com.deingun.bankingsystem.utils.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/accountholders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolder findById(@PathVariable(name = "id") Long id) {
        return userService.findById(id);
    }

    @GetMapping("/accountholders")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> findAllAccountHolders() {
        return userService.findAllAccountHolders();
    }

    @PostMapping("/accountholders")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@RequestBody @Valid AccountHolderDTO accountHolderDTO) {
        return userService.createAccountHolder(accountHolderDTO.getUsername(), accountHolderDTO.getPassword(), accountHolderDTO.getName(), accountHolderDTO.getNif(), accountHolderDTO.getDateOfBirth(),
                accountHolderDTO.getStreet(), accountHolderDTO.getCity(), accountHolderDTO.getCountry(), accountHolderDTO.getPostalCode(), accountHolderDTO.getMailingAddress());
    }

    @Override
    public void updateAccountHolder(Long id, String username, String password, String name, String nif, LocalDate dateOfBirth, Address address, String mailingAddress) {

    }

    @Override
    public void deleteAccountHolder(Long id) {

    }

    @PostMapping("/users/accountholders")
    @ResponseStatus(HttpStatus.CREATED)
    public User createAccountHolder(String username, String password, LocalDate passwordDate, String name, String nif, LocalDate dateOfBirth, Address address, String mailingAddress) {
        return null;
    }
}
