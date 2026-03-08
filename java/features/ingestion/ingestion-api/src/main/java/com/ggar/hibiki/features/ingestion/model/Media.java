package com.ggar.hibiki.features.ingestion.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an ingested media file within the system.
 *
 * <p>Stores metadata about the uploaded file such as its MIME type, size, upload timestamp,
 * and the user who uploaded it.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    private UUID id;
    private String filename;
    private String mimeType;
    private Long size;
    private String status;
    private Instant uploadedAt;
    private User uploadedBy;
}
