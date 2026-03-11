package com.ggar.hibiki.features.ingestion.model;

import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Aggregate root for the upload process. This is a domain entity persisted in Neo4j,
 * not an HTTP session. It tracks the lifecycle of a batch upload operation.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
@With
public class UploadSession {
    UploadSessionId id;
    User userId;
    List<UploadItem> items;
    IngestionPhase phase;
    Instant createdAt;
    Instant completedAt;
}
