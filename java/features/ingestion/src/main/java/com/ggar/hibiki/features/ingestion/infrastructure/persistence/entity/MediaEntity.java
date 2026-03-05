package com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.Instant;
import java.util.UUID;

@Node("Media")
public class MediaEntity {

    @Id
    private UUID id;

    @Property("filename")
    private String filename;

    @Property("mimeType")
    private String mimeType;

    @Property("size")
    private Long size;

    @Property("status")
    private String status;

    @Property("uploadedAt")
    private Instant uploadedAt;

    @Relationship(type = "UPLOADED_BY", direction = Relationship.Direction.OUTGOING)
    private UserEntity uploadedBy;

    public MediaEntity() {
    }

    public MediaEntity(UUID id, String filename, String mimeType, Long size, String status, Instant uploadedAt,
            UserEntity uploadedBy) {
        this.id = id;
        this.filename = filename;
        this.mimeType = mimeType;
        this.size = size;
        this.status = status;
        this.uploadedAt = uploadedAt;
        this.uploadedBy = uploadedBy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public UserEntity getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UserEntity uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
