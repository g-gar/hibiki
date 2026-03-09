package com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("UploadItem")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UploadItemEntity {

    @Id
    private UUID id;

    @Property("originalFilename")
    private String originalFilename;

    @Property("mimeType")
    private String mimeType;

    @Property("expectedSize")
    private long expectedSize;

    @Property("receivedChunks")
    private int receivedChunks;

    @Property("totalChunks")
    private int totalChunks;

    @Property("phase")
    private String phase;

    @Property("mediaId")
    private UUID mediaId;

    @Property("accumulatedHash")
    private String accumulatedHash;

    @Property("error")
    private String error;
}
