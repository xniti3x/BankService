package com.bankservice.Bank.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BankService {

    @Autowired private UserRepositiory userRepositiory;
    @Autowired private SettingRepository settingRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private Environment env;
    
    private RestTemplate restTemplate;
    
    private int counter = 0;

    public String getAccessToken(){
        User user = getUserEntity();
        String url = settingRepository.findByName(BankingLink.ACESS_TOKEN.toString());
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.createObjectNode()
                        .put("secret_id",user.getSecret_id())
                        .put("secret_key", user.getSecret_key()).toString();
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info("try to fetch token");
        HttpEntity<String> request =  new HttpEntity<String>(json, headers);

        Token token = restTemplate.postForObject(url, request, Token.class);
        user.setToken(token);
        log.info("saving token.");
        userRepositiory.save(user);
        
    
        return "token erfolgreich gespeichert. <a href='"+getBaseUrl()+"/choosebank'>weiter</a>";
    }

    public boolean getAccessTokenWithRefresh(){
        User user = getUserEntity();
        String url = settingRepository.findByName(BankingLink.REFRESH_TOKEN.toString());
        Token oldToken = user.getToken();
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.createObjectNode()
                        .put("refresh",oldToken.getRefresh()).toString();
        
                        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request =  new HttpEntity<String>(json, headers);

        log.info("try fetch refreshing token.");
        Token newToken = restTemplate.postForObject(url, request, Token.class);
        
        oldToken.setAccess(newToken.getAccess());
        oldToken.setAccess_expires(newToken.getAccess_expires());
        newToken=oldToken;
        user.setToken(newToken);
        log.info("saving refreshed token.");
        return userRepositiory.save(user) !=null;
    }

    public String chooseBank(){
        User user = getUserEntity();
        Token token = user.getToken();
        String url = settingRepository.findByName(BankingLink.CHOSE_BANK.toString());
        restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ token.getAccess());
        HttpEntity<String> request =  new HttpEntity<String>(headers);
        
        log.info("choseBank");
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
        String url = settingRepository.findByName(BankingLink.CREATE_AGREEMENT.toString());
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
        log.info("create agreement");
        HttpEntity<String> request =  new HttpEntity<String>(body, headers);
        Agreement agreement = restTemplate.postForObject(url, request, Agreement.class);
       
        user.setAgreement(agreement);
        return userRepositiory.save(user).toString();
    }

    public String buildLink(){
        User user = getUserEntity();
        String url = settingRepository.findByName(BankingLink.BUILD_LINK.toString());

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
        
        log.info("build link.");
        HttpEntity<String> request =  new HttpEntity<String>(body, headers);
        Requisition req = restTemplate.postForObject(url, request, Requisition.class);
        user.setRequisition(req);
        return userRepositiory.save(user).toString();
    }

    public String listAcc(){
        
        User user = getUserEntity();
        String url = settingRepository.findByName(BankingLink.LIST_ACCOUNT.toString()) + user.getRequisition().getId();
        restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer "+user.getToken().getAccess());
        
        HttpEntity<String> request =  new HttpEntity<String>(headers);
        Account acc = restTemplate.exchange(url, HttpMethod.GET, request, Account.class).getBody();
        user.setAccount(acc);
        log.info("list acc");
        return userRepositiory.save(user).toString();
    }

    @Scheduled(cron = "${cronjob}", zone = "Europe/Berlin")
    public List<Transaction> accessAccountData() {
        User user = getUserEntity();
        String url = settingRepository.findByName(BankingLink.ACCESS_TRANSACTION.toString())+user.getAccount().getAccounts().get(0)+"/transactions/";
        
        restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer "+user.getToken().getAccess());
        
        HttpEntity<String> request =  new HttpEntity<String>(headers);
        try {
            log.info("fetching transactions...");
            ResponseEntity<String> transactions= restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            
            ObjectMapper mapper = new ObjectMapper();
            TransactionDto dto = mapper.readValue(transactions.getBody(),TransactionDto.class);
            List<Transaction> bookedTransactions = dto.getTransactions().getBooked().stream().map(t->convertToTransaction(t)).toList();
            List<Transaction> list = transactionRepository.findAll().stream().distinct().filter(t->!bookedTransactions.contains(t)).toList();

            list.forEach(t->transactionRepository.save(t));
            counter = 0;
            return list;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            
            if(e.getStatusCode().value()==401){
                boolean isRefreshed = getAccessTokenWithRefresh();
                if(isRefreshed && counter <= 1){
                    log.info("try fetching transactions with refreshed token.");
                    accessAccountData();
                    counter ++;
                }
            }else{
                log.info("failed -> statusCode:"+e.getStatusCode());
                log.info(e.getMessage());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
         
        counter = 0;   
        return null;
    }

    private void selectNextUser(User currentUser) {
        var userList=userRepositiory.findAll();
        for (int i=0; i<userList.size(); i++) {
            if(userList.get(i).getId()==currentUser.getId()){
                int nextIndex=i+1;
                if(userList.size()<=nextIndex) {
					settingRepository.setName(""+userList.get(0).getId());
            	}else{
					settingRepository.setName(""+userList.get(nextIndex).getId());
				}
			}
        }
    }

    private static Transaction convertToTransaction(Booked tr){
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
        return entity;
    }
    private User getUserEntity(){
        return userRepositiory.findAll().get(0);
    }
    private String getBaseUrl(){
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }
    


}
