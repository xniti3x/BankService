package com.bankservice.Bank.ui;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bankservice.Bank.model.TransactionDto;
import com.bankservice.Bank.repository.TransactionRepository;
import com.bankservice.Bank.service.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@Route("files") // root path
@Slf4j
public class FileView extends VerticalLayout {

    private final TransactionRepository transactionRepository;
    
    public FileView(TransactionRepository transactionRepository) throws Exception{
        this.transactionRepository = transactionRepository;
            Grid<File> fileGrid = new Grid<>();
            fileGrid.setWidth("400px");
             fileGrid.addColumn(f->f.getName()).setHeader("Filename");
             fileGrid.addColumn(new ComponentRenderer<>(f -> {
                Button btn = new Button("->");
                btn.addClickListener(e->{
                try{    
                     ObjectMapper mapper = new ObjectMapper();
                    TransactionDto dto = mapper.readValue(f,TransactionDto.class);
                    var dbList = transactionRepository.findAll();
                    dto.getTransactions().getBooked().stream()
                        .map(t->BankService.convertToTransaction(t))
                        .filter(t->!dbList.contains(t))
                        .forEach(transactionRepository::save);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                });
                return btn;
            })).setHeader("RUN");
             String userDir = System.getProperty("user.dir");
            Path parentPath = Paths.get(userDir).getParent();
            String pdfURL = parentPath+"/paperless-ngx/media/documents/archive/";
            var files = findJsonFilesInRoot(pdfURL).stream().map(f->f.toFile()).toList();
            fileGrid.setItems(files);
            add(fileGrid);
    }
                
    public static List<Path> findJsonFilesInRoot(String rootDirectory) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(rootDirectory), 1)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .collect(Collectors.toList());
        }
    }
}