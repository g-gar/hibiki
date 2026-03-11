package com.ggar.hibiki.features.ingestion.model;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Immutable domain model representing a persisted media file.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
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
