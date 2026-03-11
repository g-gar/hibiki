package com.ggar.hibiki.features.ingestion.model;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * DTO emitted via SSE to report upload progress to the client.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class UploadProgress {
    UUID uploadSessionId;
    UUID itemId;
    IngestionPhase phase;
    int progress;
    UUID mediaId;
    String error;
}
