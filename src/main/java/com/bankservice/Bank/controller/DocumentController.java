package com.bankservice.Bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankservice.Bank.service.DocumentService;

@RestController
public class DocumentController {

    @Autowired private DocumentService documentService;

    @GetMapping("/map")
    public void findAndAutoMatch(){
        documentService.findAndAutoMatch();
    }

    
}
