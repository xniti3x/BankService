package com.bankservice.Bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankservice.Bank.entity.Document;

public interface DocumentRepository extends JpaRepository<Document,Integer>{

    List<Document> findByTransactionIsNull();
}
