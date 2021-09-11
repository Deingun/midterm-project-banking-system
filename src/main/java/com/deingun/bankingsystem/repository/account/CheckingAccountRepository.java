package com.deingun.bankingsystem.repository.account;

import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount,Long> {

    Optional<CheckingAccount> findById(Long id);

    List<CheckingAccount> findByPrimaryOwnerId(Long id);

    List<CheckingAccount> findAll();
}
