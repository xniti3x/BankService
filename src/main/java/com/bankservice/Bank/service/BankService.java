package com.bankservice.Bank.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bankservice.Bank.entity.Account;
import com.bankservice.Bank.entity.Agreement;
import com.bankservice.Bank.entity.Institution;
import com.bankservice.Bank.entity.Requisition;
import com.bankservice.Bank.entity.Token;
import com.bankservice.Bank.entity.Transaction;
import com.bankservice.Bank.entity.User;
import com.bankservice.Bank.repository.SettingRepository;
import com.bankservice.Bank.repository.TransactionRepository;
import com.bankservice.Bank.repository.UserRepositiory;
import com.bankservice.Bank.util.BankingLink;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BankService {

    @Autowired private UserRepositiory userRepositiory;
    @Autowired private SettingRepository settingRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private Environment env;

    

    private RestTemplate restTemplate;


    public String getAccessToken(){
        User user = getUserEntity();
        String url = settingRepository.findByName(BankingLink.ACESS_TOKEN.value());
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.createObjectNode()
                        .put("secret_id",user.getSecret_id())
                        .put("secret_key", user.getSecret_key()).toString();
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request =  new HttpEntity<String>(json, headers);

        Token token = restTemplate.postForObject(url, request, Token.class);
        user.setToken(token);
        userRepositiory.save(user);
        
    
        return "token erfolgreich gespeichert. <a href='"+getBaseUrl()+"/choosebank'>weiter</a>";
    }

    public String chooseBank(){
        User user = getUserEntity();
        Token token = user.getToken();
        String url = settingRepository.findByName(BankingLink.CHOSE_BANK.value());
        restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ token.getAccess());
        HttpEntity<String> request =  new HttpEntity<String>(headers);
        
        Institution[] ins = restTemplate.exchange( url, HttpMethod.GET, request, Institution[].class).getBody();
        
        for (Institution institution : ins) {
            if(institution.getId().equals(env.getProperty("institutionId"))){
                user.setInstitution(institution);
            }
        }
        
        return "in application.properties ändere institutionId und dan <a href='"+getBaseUrl()+"/createagreement'>weiter</a> <br>"+Arrays.toString(ins);
    }

    public String createAgreement(){
        
        User user = getUserEntity();
        Token token = user.getToken();
        Institution institution = user.getInstitution();
        String url = settingRepository.findByName(BankingLink.CREATE_AGREEMENT.value());
        restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer "+token.getAccess());
        
        ObjectMapper mapper = new ObjectMapper();

        String body = mapper.createObjectNode()
                        .put("institution_id",institution.getId())
                        .put("max_historical_days", settingRepository.findByName("max_historical_days"))
                        .put("access_valid_for_days", settingRepository.findByName("access_valid_for_days"))
                        //.put("access_scope", Arrays.asList("transaction").toString())
                        .toString();
        
        System.out.println(body);
        HttpEntity<String> request =  new HttpEntity<String>(body, headers);
        Agreement agreement = restTemplate.postForObject(url, request, Agreement.class);
       
        user.setAgreement(agreement);
        return userRepositiory.save(user).toString();
    }

    public String buildLink(){
        User user = getUserEntity();
        String url = settingRepository.findByName(BankingLink.BUILD_LINK.value());

        restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer "+user.getToken().getAccess());
        
        ObjectMapper mapper = new ObjectMapper();

        String body = mapper.createObjectNode()
                        .put("redirect","https://www.google.de")
                        .put("institution_id",user.getInstitution().getId())
                        //.put("reference","123")
                        .put("agreement", user.getAgreement().getId())
                        .put("user_language", "DE")
                        .toString();
        
        
        HttpEntity<String> request =  new HttpEntity<String>(body, headers);
        Requisition req = restTemplate.postForObject(url, request, Requisition.class);
        user.setRequisition(req);
        return userRepositiory.save(user).toString();
    }

    public String listAcc(){
        
        User user = getUserEntity();
        String url = settingRepository.findByName(BankingLink.LIST_ACCOUNT.value()) + user.getRequisition().getId();
        System.out.println(url);
        restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer "+user.getToken().getAccess());
        
        HttpEntity<String> request =  new HttpEntity<String>(headers);
        Account acc = restTemplate.exchange(url, HttpMethod.GET, request, Account.class).getBody();
        user.setAccount(acc);
        return userRepositiory.save(user).toString();
    }

    public String accessAccountData() throws JsonMappingException, JsonProcessingException{
        User user = getUserEntity();
        String url = settingRepository.findByName(BankingLink.ACCESS_TRANSACTION.value())+user.getAccount().getAccounts().get(0)+"/transactions/";
        
        restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer "+user.getToken().getAccess());
        
        HttpEntity<String> request =  new HttpEntity<String>(headers);
        String transactions= restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
        System.out.println(transactions);
        ObjectMapper mapper = new ObjectMapper();
        TransactionDto dto = mapper.readValue(transactions,TransactionDto.class);
        ArrayList<Booked> bookedTransactions = dto.getTransactions().getBooked();
        
        Transaction last = transactionRepository.findLast().orElse(new Transaction());
        for (Booked tr : bookedTransactions) {
            if(tr.getTransactionId().equals(last.getTransactionId())) break;
            Transaction entity = new Transaction();
            entity.setTransactionId(tr.getTransactionId());
            entity.setBookingDate(tr.getBookingDate());
            entity.setValueDate(tr.getValueDate());
            entity.setAmount(tr.getTransactionAmount().getAmount());
            if(tr.getCreditorName()!=null){
                entity.setCompanyName(tr.getCreditorName());
            }else if(tr.getDebtorName()!=null){
                entity.setCompanyName(tr.getDebtorName());
            }else{
                entity.setCompanyName("NO TITLE");
            }
            if(tr.getDebtorAccount()!=null) {
                entity.setIban(tr.getDebtorAccount().getIban());
            }else{
                entity.setIban("NO IBAN");
            }
            entity.setDescription(tr.getRemittanceInformationStructured());
            entity.setAdditionalInformation(tr.getAdditionalInformation());
            transactionRepository.save(entity);
        }

        return transactions;
    }

    private User getUserEntity(){
        return userRepositiory.findById(1).get();
    }

    private String getBaseUrl(){
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }
    


}
