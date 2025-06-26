package com.bankservice.Bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentDTO {
    
    private Integer id;
    private String title;
    private String content;
    private String modified;
    private Integer correspondentId;
    private String checksum;
    private String added;
    private String storageType;
    private String filename;
    private Integer archiveSerialNumber;
    private Integer documentTypeId;
    private String mimeType;
    private String archiveChecksum;
    private String archiveFilename;
    private Integer storagePathId;
    private String originalFilename;
    private Integer ownerId;
    private String deletedAt;
    private String restoredAt;
    private String transactionId; // UUID stored as String
    private Long pageCount;
    private String created;
    private String valueText;
    
}
