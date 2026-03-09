package com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Media")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MediaEntity {

    @Id
    private UUID id;

    @Property("filename")
    private String filename;

    @Property("mimeType")
    private String mimeType;

    @Property("status")
    private String status;

    @Property("contentHash")
    private String contentHash;

    @Property("uploadedAt")
    private Instant uploadedAt;

    @Relationship(type = "UPLOADED_BY", direction = Relationship.Direction.OUTGOING)
    private UserEntity uploadedBy;
}
