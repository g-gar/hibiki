package com.ggar.hibiki.features.ingestion.model;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Immutable domain model representing a persisted media file.
 */
@Value
@Builder(toBuilder = true)
@With
public class Media {
    MediaId id;
    String filename;
    String mimeType;
    MediaStatus status;
    String contentHash;
    Instant uploadedAt;
    User uploadedBy;
}
