package com.bankservice.Bank.ui;

import java.io.FileNotFoundException;
import com.bankservice.Bank.entity.Transaction;
import com.bankservice.Bank.repository.TransactionRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

@Route("document") // root path
@Slf4j
public class DocumentView extends VerticalLayout {
    
    private final TransactionRepository transactionService;
    
    private final Grid<Transaction> transactionGrid = new Grid<>(Transaction.class, false);

    
    public DocumentView(TransactionRepository transactionService) throws FileNotFoundException{
         this.transactionService = transactionService;
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSizeFull();
        VerticalLayout left = new VerticalLayout();
        VerticalLayout right = new VerticalLayout();
        left.setSizeFull();
        right.setSizeFull();
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Details",new Div(transactionGrid));
        tabSheet.add("Content",new Div(new Text("This is the Payment tab content")));
        add(tabSheet);
      
        configureTransactionGrid();
        transactionGrid.setItems(transactionService.findTransactionsNotUsedInDocuments());

        IFrame frame = new IFrame("https://pdfobject.com/pdf/sample.pdf");    
        frame.setSizeFull();

        left.add(tabSheet);
        right.add(frame);
        
        setSizeFull();
        hl.add(left,right);
        add(hl);
    }
private void configureTransactionGrid() {
        transactionGrid.addColumn(Transaction::getCompanyName).setHeader("Company");
        transactionGrid.addColumn(Transaction::getDescription).setHeader("Description");
        transactionGrid.addColumn(Transaction::getAmount).setHeader("Amount");
    }
}
