package com.bankservice.Bank.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankservice.Bank.entity.Transaction;
import com.bankservice.Bank.model.DocumentDTO;
import com.bankservice.Bank.repository.SecondaryDao;
import com.bankservice.Bank.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentService {

    @Autowired private SecondaryDao documentDao;
    @Autowired private TransactionRepository transactionRepository;

    public void findAndAutoMatch() {
        // fetch all transaction with no document
        List<Transaction> allTransactions = transactionRepository.findAll();

        // fetch all documents with no transaction
        List<DocumentDTO> allDocuments = documentDao.findAll();

        // get transaction [company name, price amount, invocieNr, date is after]
        for (Transaction t : allTransactions) {

            for (DocumentDTO d : allDocuments) {
                double score = 0;
                String content = d.getContent();
                if (content.contains(t.getAmount())) {
                    score += 0.3;
                    log.info("amount found score" + score);
                }

                if(t.getDescription().contains(d.getValueText())){
                    score += 0.3;
                    log.info("amount found score" + score);
                }
                
                if(t.getAmount().contains(d.getValueText())){
                    score += 0.3;
                    log.info("amount found score" + score);
                }
                System.out.println("score for document "+d.getTitle()+": "+score);
                
            }
        }
        // get document content

        // check if 1 criteria maches if 2, if 3 add a certain score foreach
        // if score is greater than 0 assing the document to transaction
    }

    /**
     * Converts the transaction description text to a regex based pattern defined
     * for that specific company.
     * 
     * @param description
     * @return
     */
    private String fetchWithRegex(String description) {
        return description;
    }

    private String getDocumentContent(int documentId) {
        // ToDo:
        Map<Integer, String> doc = new HashMap<>();
        doc.put(1, "content 1 asdbv");
        doc.put(2, "content 2 asd");
        doc.put(3, "content  3 cedea");
        doc.put(4, "content 4 dasdw");
        return doc.get(documentId);
    }

    public List<DocumentDTO> findAll(){
        return documentDao.findAll();
    }

    public int save(DocumentDTO doc) {
        return documentDao.save(doc);
    }
}
