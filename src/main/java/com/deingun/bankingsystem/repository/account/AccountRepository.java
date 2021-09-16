package com.deingun.bankingsystem.repository.account;

import com.deingun.bankingsystem.model.account.Account;
import com.deingun.bankingsystem.model.account.CheckingAccount;
import com.deingun.bankingsystem.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findById(Long id);

    List<Account> findByPrimaryOwnerId(Long id);

    List<Account> findByPrimaryOwnerIdOrSecondaryOwnerId(Long primaryOwnerId, Long secondaryOwnerId);

    List<Account> findBySecondaryOwnerId(Long id);

    List<Account> findAll();
}
