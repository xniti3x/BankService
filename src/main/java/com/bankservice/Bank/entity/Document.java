package com.bankservice.Bank.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

    private String title;
    @Lob
    @Column(name = "content", columnDefinition = "TEXT", nullable = true)
    private String content;
    private String created;
    private String modified;
    @Column(nullable = true)
    private Integer correspondent_id;
    private String checksum;
    private String added;
    private String storage_type;
    @Column(nullable = true)
    private String filename;
    @Column(nullable = true)
    private Integer archive_serial_number;
    @Column(nullable = true)
    private Integer document_type_id;
    private String mime_type;
    @Column(nullable = true)
    private String archive_checksum;
    @Column(nullable = true)
    private String archive_filename;
    @Column(nullable = true)
    private Integer storage_path_id;
    @Column(nullable = true)
    private String original_filename;
    @Column(nullable = true)
    private Integer owner_id;
    @Column(nullable = true)
    private String deleted_at;
    @Column(nullable = true)
    private String restored_at;
    @Column(nullable = true)
    private Integer page_count;    
    
}
