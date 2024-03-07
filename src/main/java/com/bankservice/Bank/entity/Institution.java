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
public class Institution{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int institutionId;
    private String id;
    private String name;
    private String bic;
    private String transaction_total_days;
    private ArrayList<String> countries;
    private String logo;
}


