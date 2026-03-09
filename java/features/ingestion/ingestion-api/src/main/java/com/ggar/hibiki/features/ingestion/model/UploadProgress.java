package com.ggar.hibiki.features.ingestion.model;

import lombok.Builder;
import lombok.Value;

/**
 * DTO emitted via SSE to report upload progress to the client.
 */
@Value
@Builder
public class UploadProgress {
    UploadSessionId uploadSessionId;
    UploadItemId itemId;
    IngestionPhase phase;
    int progress;
    MediaId mediaId;
    String error;
}
