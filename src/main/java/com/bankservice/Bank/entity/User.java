package com.bankservice.Bank.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String secret_id;
    private String secret_key;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Institution institution;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Token token;

    @OneToOne(cascade = CascadeType.ALL)
    private Requisition requisition;
    @OneToOne(cascade = CascadeType.ALL)
    private Agreement agreement;
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;
}
