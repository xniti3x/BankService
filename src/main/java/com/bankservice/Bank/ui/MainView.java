package com.bankservice.Bank.ui;

import java.util.Set;
import com.bankservice.Bank.entity.Document;
import com.bankservice.Bank.entity.Transaction;
import com.bankservice.Bank.repository.DocumentRepository;
import com.bankservice.Bank.repository.TransactionRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@Route("") // root path
@Slf4j
public class MainView extends VerticalLayout { 
    
    private final TransactionRepository transactionService;
    private final DocumentRepository documentService;

    private final Grid<Transaction> transactionGrid = new Grid<>(Transaction.class, false);
    private final Grid<Document> documentGrid = new Grid<>(Document.class, false);
    private final VerticalLayout btnContent = new VerticalLayout();

    public MainView(TransactionRepository transactionService, DocumentRepository documentService) {
        this.transactionService = transactionService;
        this.documentService = documentService;

        setupLayout();
        configureTransactionGrid();
        configureDocumentGrid();

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

        left.add(transactionGrid);
        right.add(btnContent);
        right.add(documentGrid);
        layout.add(left, right);
        add(layout);
        setSizeFull();
    }

    private void configureTransactionGrid() {
        transactionGrid.addColumn(Transaction::getCompanyName).setHeader("Company");
        transactionGrid.addColumn(Transaction::getDescription).setHeader("Description");
        transactionGrid.addColumn(Transaction::getAmount).setHeader("Amount");
        
        transactionGrid.asSingleSelect().addValueChangeListener(event -> {
            Transaction selectedTransaction = event.getValue();
            if (selectedTransaction != null) {
                Button assignButton = new Button("Assign Documents", click -> {
                    Set<Document> selectedDocs = documentGrid.getSelectedItems();
                    selectedDocs.forEach(doc -> {
                        doc.setTransaction(selectedTransaction);
                        documentService.save(doc);
                    });
                    refreshGrids();
                });
                assignButton.setEnabled(!documentGrid.getSelectedItems().isEmpty());
                btnContent.removeAll();
                btnContent.add(assignButton);
            }
        });
    }

    private void configureDocumentGrid() {
        documentGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        documentGrid.setWidthFull();
        documentGrid.setAllRowsVisible(true);
        //documentGrid.addColumn(Document::getContent).setHeader("content");
        documentGrid.addColumn(new ComponentRenderer<>(document -> {
        TextArea textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setLabel("Description");
        textArea.setValue(document.getContent());
        return new Div(textArea);
})).setHeader("Document Info").setAutoWidth(true);
    }

    private void refreshGrids() {
        transactionGrid.setItems(transactionService.findTransactionsNotUsedInDocuments());
        documentGrid.setItems(documentService.findByTransactionIsNull());
    }
}