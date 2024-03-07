package com.bankservice.Bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankservice.Bank.service.BankService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class BankingController {

    @Autowired BankService bankService;


    @GetMapping("/getaccesstoken")
    public String getAccessToken(){
        return bankService.getAccessToken();
        
    }

    @GetMapping("/choosebank")
    public String chooseBank(){
        return bankService.chooseBank();
    }

    @GetMapping("/createagreement")
    public String createAgreement(){
        return bankService.createAgreement();
        
    }

    @GetMapping("/buildlink")
    public String buildLink(){
        
        return bankService.buildLink();
    }

    @GetMapping("/listaccounts")
    public String listAccounts(){
        
        return bankService.listAcc();
    }

    @GetMapping("/accessbankdata")
    public String accessBankData() throws JsonMappingException, JsonProcessingException{
        
        return bankService.accessAccountData();
    }

    
}
