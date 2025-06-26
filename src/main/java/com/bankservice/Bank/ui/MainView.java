package com.bankservice.Bank.ui;

import com.bankservice.Bank.entity.Transaction;
import com.bankservice.Bank.model.DocumentDTO;
import com.bankservice.Bank.repository.TransactionRepository;
import com.bankservice.Bank.service.DocumentService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@Route("") // root path
@Slf4j
public class MainView extends VerticalLayout { 
    
    private final TransactionRepository transactionService;
    private final DocumentService documentService;

    private final Grid<Transaction> transactionGrid = new Grid<>(Transaction.class, false);
   
    private final VerticalLayout btnContent = new VerticalLayout();

    public MainView(TransactionRepository transactionService, DocumentService documentService) {
        this.transactionService = transactionService;
        this.documentService = documentService;

        setupLayout();
        configureTransactionGrid();

        refreshGrids();
    }

    private void setupLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        VerticalLayout left = new VerticalLayout();
        VerticalLayout right = new VerticalLayout();

        layout.setSizeFull();
        left.setWidth("35%");
        right.setWidth("65%");

        //transactionGrid.setWidth("50%");
        //documentGrid.setWidth("50%");
        DocumentDTO documentDTO = documentService.findAll().get(0);
        String pdfURL = System.getProperty("user.dir")+"/../paperless-ngx/media/documents/archive/"+documentDTO.getFilename();
        Html iframe = new Html("<iframe src='" + pdfURL + "' width='100%' height='100%'></iframe>");
        
        left.add(transactionGrid);
        right.add(btnContent);
        right.add(iframe);
        layout.add(left, right);
        add(layout);
        setSizeFull();
    }

    private void configureTransactionGrid() {
        transactionGrid.addColumn(Transaction::getCompanyName).setHeader("Company");
        transactionGrid.addColumn(Transaction::getDescription).setHeader("Description");
        transactionGrid.addColumn(Transaction::getAmount).setHeader("Amount");
        


        /*Button assignButton = new Button("Assign Documents", click -> {
                    Set<DocumentDTO> selectedDocs = documentGrid.getSelectedItems();
                    selectedDocs.forEach(doc -> {
                        doc.setTransactionId(selectedTransaction.getTransactionId());
                        documentService.save(doc);
                    });
                    refreshGrids();
                });
                */
    }


    private void refreshGrids() {
        transactionGrid.setItems(transactionService.findTransactionsNotUsedInDocuments());
    }
}