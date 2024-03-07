package com.bankservice.Bank.entity;

import java.time.LocalDate;
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
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int agreementId;
    public String id;
    public LocalDate created;
    public int max_historical_days;
    public int access_valid_for_days;
    public ArrayList<String> access_scope;
    public String accepted;
    public String institution_id;
}


