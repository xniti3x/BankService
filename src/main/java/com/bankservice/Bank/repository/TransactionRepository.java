package com.bankservice.Bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bankservice.Bank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Query("SELECT t FROM Transaction t ORDER BY t.id ASC LIMIT 1")
    Optional<Transaction> findLast();
    Optional<Transaction> findFirstByOrderByIdDesc();
}

