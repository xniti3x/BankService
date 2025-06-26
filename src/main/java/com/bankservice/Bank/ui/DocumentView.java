package com.bankservice.Bank.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.bankservice.Bank.entity.Transaction;
import com.bankservice.Bank.model.DocumentDTO;
import com.bankservice.Bank.repository.TransactionRepository;
import com.bankservice.Bank.service.DocumentService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import lombok.extern.slf4j.Slf4j;

@Route("document") // root path
@Slf4j
public class DocumentView extends VerticalLayout {
    
    private final TransactionRepository transactionService;
    
    private final Grid<Transaction> transactionGrid = new Grid<>(Transaction.class, false);
    private final DocumentService documentService;
    private int startDocument = 0;    
    public DocumentView(TransactionRepository transactionService, DocumentService documentService) throws IOException{
         this.transactionService = transactionService;
         this.documentService = documentService;
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
        
        HorizontalLayout btns = new HorizontalLayout();
        left.add(btns);
        left.add(tabSheet);

        IFrame frame = new IFrame();
        frame.setSizeFull();
        var docList = documentService.findAll();
        
        if(docList.size()>0 && startDocument>=0 && startDocument<docList.size()){
            var resource = getResource(docList,startDocument);
            frame.getElement().setAttribute("src", resource);
        }

        Button previus = new Button("<-",e->{
            try {
                startDocument=startDocument-1;
                if(docList.size()>0 && startDocument>=0 && startDocument<docList.size()){
                    var resource = getResource(docList,startDocument);
                    frame.getElement().setAttribute("src", resource);
                }else{startDocument=0;}
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
   
        });
        Button next = new Button("->",e->{
            try {
                startDocument=startDocument+1;
                if(docList.size()>0 && startDocument>=0 && startDocument<docList.size()){
                    var resource = getResource(docList,startDocument);
                    frame.getElement().setAttribute("src", resource);
                }else{startDocument=docList.size()-1;}
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
   
        });

        btns.add(previus,next);
        
        

        // Step 4: Set the src of the IFrame

        
        right.add(frame);
        
        setSizeFull();
        hl.add(left,right);
        add(hl);
    }
    private StreamResource getResource(List<DocumentDTO> docList,int index) throws IOException {
        DocumentDTO documentDTO = docList.get(index);
        String pdfURL = System.getProperty("user.dir")+"/media/documents/archive/"+ documentDTO.getFilename();
        var arry = Files.readAllBytes(Path.of(pdfURL));
        return new StreamResource(documentDTO.getFilename(), () -> new ByteArrayInputStream(arry));
        
    }
private void configureTransactionGrid() {
        transactionGrid.addColumn(Transaction::getCompanyName).setHeader("Company");
        transactionGrid.addColumn(Transaction::getDescription).setHeader("Description");
        transactionGrid.addColumn(Transaction::getAmount).setHeader("Amount");
    }
}
