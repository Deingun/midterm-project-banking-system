package com.deingun.bankingsystem.controller.impl;

import com.deingun.bankingsystem.controller.dto.CheckingAccountDTO;
import com.deingun.bankingsystem.controller.dto.SavingAccountDTO;
import com.deingun.bankingsystem.controller.interfaces.AccountController;
import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.User;
import com.deingun.bankingsystem.security.CustomUserDetails;
import com.deingun.bankingsystem.service.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountControllerImpl implements AccountController {

    @Autowired
    AccountService accountService;


    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> findAllAccounts(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return accountService.findAllAccounts(customUserDetails);
    }

    @PostMapping("/checkingaccounts")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createCheckingAccount(@RequestBody @Valid CheckingAccountDTO checkingAccountDTO) {
        return accountService.createCheckingAccount(checkingAccountDTO.getEntityNumber(), checkingAccountDTO.getBranchNumber(),
                checkingAccountDTO.getAmount(), checkingAccountDTO.getPrimaryOwnerId(),
                checkingAccountDTO.getSecondaryOwnerId(), checkingAccountDTO.getSecretKey());
    }

    @PostMapping("/savingaccounts")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createSavingAccount(@RequestBody @Valid SavingAccountDTO savingAccountDTO) {
        return accountService.createSavingAccount(savingAccountDTO.getEntityNumber(), savingAccountDTO.getBranchNumber(), savingAccountDTO.getAmount(),
                savingAccountDTO.getPrimaryOwnerId(), savingAccountDTO.getSecondaryOwnerId(), savingAccountDTO.getSecretKey(), savingAccountDTO.getMinimumBalance(),
                savingAccountDTO.getInterestRate());
    }
}
