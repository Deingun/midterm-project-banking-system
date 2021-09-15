package com.deingun.bankingsystem.controller.interfaces;

import com.deingun.bankingsystem.controller.dto.AccountHolderDTO;
import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.utils.Address;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public interface UserController {

    List<User> findAll();

    AccountHolder findById(Long id);

    List<AccountHolder> findAllAccountHolders();

    AccountHolder createAccountHolder(AccountHolderDTO accountHolderDTO);

    void updateAccountHolder(Long id, String username, String password, String name, String nif, LocalDate dateOfBirth, Address address, String mailingAddress);

    void deleteAccountHolder(Long id);
}
