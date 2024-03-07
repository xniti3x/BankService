package com.bankservice.Bank.entity;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Requisition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requisitionId;
 
    public String id;
    public String redirect;
    public String status;
    public String agreement;
    public ArrayList<Account> accounts;
    public String reference;
    public String user_language;
    public String link;
}

