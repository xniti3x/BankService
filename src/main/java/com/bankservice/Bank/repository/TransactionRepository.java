package com.bankservice.Bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bankservice.Bank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Query("SELECT t FROM Transaction t ORDER BY t.id DESC LIMIT 1")
    Optional<Transaction> findLast();

    @Query("SELECT t FROM Transaction t WHERE t.id NOT IN (SELECT DISTINCT d.transaction.id FROM Document d WHERE d.transaction IS NOT NULL)")
    List<Transaction> findTransactionsNotUsedInDocuments();
}

