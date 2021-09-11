package com.deingun.bankingsystem.repository;

import com.deingun.bankingsystem.model.user.AccountHolder;
import com.deingun.bankingsystem.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder,Long> {

    List<AccountHolder> findAll();
}
