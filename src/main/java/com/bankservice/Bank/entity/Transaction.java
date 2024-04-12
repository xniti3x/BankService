package com.bankservice.Bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String transactionId;
    private String bookingDate;
    private String valueDate;
    private String amount;
    private String companyName;
    private String iban;
    private String description;
    private String additionalInformation;
    private int userId;
}



