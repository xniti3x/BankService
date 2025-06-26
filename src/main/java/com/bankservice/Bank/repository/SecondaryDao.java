package com.bankservice.Bank.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bankservice.Bank.model.DocumentDTO;

@Repository
public class SecondaryDao {

    private final JdbcTemplate secondaryJdbcTemplate;

    @Autowired
    public SecondaryDao(@Qualifier("secondaryJdbcTemplate") JdbcTemplate secondaryJdbcTemplate) {
        this.secondaryJdbcTemplate = secondaryJdbcTemplate;
    }

    public List<DocumentDTO> findAll() {
        return secondaryJdbcTemplate.query(
            "SELECT documents_document.*, documents_customfieldinstance.value_text FROM documents_document,documents_customfieldinstance where documents_document.id=documents_customfieldinstance.document_id AND documents_customfieldinstance.field_id = 2",
            (rs, rowNum) -> {
                DocumentDTO dto = new DocumentDTO();
                dto.setId(rs.getInt("id"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setModified(rs.getString("modified"));
                dto.setCorrespondentId(rs.getObject("correspondent_id", Integer.class));
                dto.setChecksum(rs.getString("checksum"));
                dto.setAdded(rs.getString("added"));
                dto.setStorageType(rs.getString("storage_type"));
                dto.setFilename(rs.getString("filename"));
                dto.setArchiveSerialNumber(rs.getObject("archive_serial_number", Integer.class));
                dto.setDocumentTypeId(rs.getObject("document_type_id", Integer.class));
                dto.setMimeType(rs.getString("mime_type"));
                dto.setArchiveChecksum(rs.getString("archive_checksum"));
                dto.setArchiveFilename(rs.getString("archive_filename"));
                dto.setStoragePathId(rs.getObject("storage_path_id", Integer.class));
                dto.setOriginalFilename(rs.getString("original_filename"));
                dto.setOwnerId(rs.getObject("owner_id", Integer.class));
                dto.setDeletedAt(rs.getString("deleted_at"));
                dto.setRestoredAt(rs.getString("restored_at"));
                dto.setTransactionId(rs.getString("transaction_id")); // UUID as String
                dto.setPageCount(rs.getObject("page_count", Long.class));
                dto.setCreated(rs.getString("created"));
                dto.setValueText(rs.getString("value_text"));
                return dto;
            }
        );
    }

    public int save(DocumentDTO dto) {
    String sql = "INSERT INTO documents_document (" +
            "title, content, modified, correspondent_id, checksum, added, storage_type, filename, " +
            "archive_serial_number, document_type_id, mime_type, archive_checksum, archive_filename, " +
            "storage_path_id, original_filename, owner_id, deleted_at, restored_at, transaction_id, " +
            "page_count, created) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    return secondaryJdbcTemplate.update(sql,
        dto.getTitle(),
        dto.getContent(),
        dto.getModified(),
        dto.getCorrespondentId(),
        dto.getChecksum(),
        dto.getAdded(),
        dto.getStorageType(),
        dto.getFilename(),
        dto.getArchiveSerialNumber(),
        dto.getDocumentTypeId(),
        dto.getMimeType(),
        dto.getArchiveChecksum(),
        dto.getArchiveFilename(),
        dto.getStoragePathId(),
        dto.getOriginalFilename(),
        dto.getOwnerId(),
        dto.getDeletedAt(),
        dto.getRestoredAt(),
        dto.getTransactionId(),
        dto.getPageCount(),
        dto.getCreated()
    );
}

}




