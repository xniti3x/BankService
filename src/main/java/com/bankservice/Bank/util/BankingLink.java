package com.bankservice.Bank.util;


public enum BankingLink {
    
    ACESS_TOKEN("AccessToken"),
    CHOSE_BANK("ChooseBank"),
    CREATE_AGREEMENT("CreateAgreement"),
    BUILD_LINK("BuildLink"),
    LIST_ACCOUNT("ListAccounts"),
    ACCESS_TRANSACTION("AccessTransactions");

    private String name;
    private BankingLink(String name){
        this.name=name;
    }
    
    public String value(){
        return this.name;
    }
}
