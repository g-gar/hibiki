package com.ggar.hibiki.features.ingestion.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * DTO emitted via SSE to report upload progress to the client.
 */
@Value
@Builder
public class UploadProgress {
    UUID uploadSessionId;
    UUID itemId;
    IngestionPhase phase;
    int progress;
    UUID mediaId;
    String error;
}
