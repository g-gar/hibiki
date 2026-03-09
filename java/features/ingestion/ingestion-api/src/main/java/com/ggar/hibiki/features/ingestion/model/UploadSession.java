package com.ggar.hibiki.features.ingestion.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Aggregate root for the upload process. This is a domain entity persisted in Neo4j,
 * not an HTTP session. It tracks the lifecycle of a batch upload operation.
 */
@Value
@Builder(toBuilder = true)
@With
public class UploadSession {
    UUID id;
    UUID userId;
    List<UploadItem> items;
    IngestionPhase phase;
    Instant createdAt;
    Instant completedAt;
}
