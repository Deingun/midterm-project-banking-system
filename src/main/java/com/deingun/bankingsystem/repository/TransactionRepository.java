package com.deingun.bankingsystem.repository;

import com.deingun.bankingsystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findById(Long id);

    List<Transaction> findAll();

    List<Transaction> findAllByPaymasterUsername(String username);
}
