package com.bankservice.Bank.service;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

 @AllArgsConstructor
 @NoArgsConstructor
 @Data
 class Booked{
    public String transactionId;
    public String entryReference;
    public String bookingDate;
    public String valueDate;
    public TransactionAmount transactionAmount;
    public String debtorName;
    public DebtorAccount debtorAccount;
    public String remittanceInformationStructured;
    public String additionalInformation;
    public String proprietaryBankTransactionCode;
    public String creditorAgent;
    public String debtorAgent;
    public String internalTransactionId;
    public String purposeCode;
    public CreditorAccount creditorAccount;
    public ArrayList<String> remittanceInformationStructuredArray;
    public String creditorName;
    public String ultimateDebtor;
    public String endToEndId;
    public String ultimateCreditor;
    public String mandateId;
}
 @AllArgsConstructor
 @NoArgsConstructor
 @Data
 class CreditorAccount{
    public String iban;
 }
 @AllArgsConstructor
 @NoArgsConstructor
 @Data
 class DebtorAccount{
    public String iban;
 }

 @AllArgsConstructor
 @NoArgsConstructor
 @Data
 public class TransactionDto{
    public Transactions transactions;
 }
 @AllArgsConstructor
 @NoArgsConstructor
 @Data
 class TransactionAmount{
    public String amount;
    public String currency;
 }
 @AllArgsConstructor
 @NoArgsConstructor
 @Data
 class Transactions{
    public ArrayList<Booked> booked;
    public ArrayList<Object> pending;
 }
