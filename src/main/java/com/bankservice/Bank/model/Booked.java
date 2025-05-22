package com.bankservice.Bank.model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Booked {
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