package com.ggar.hibiki.features.ingestion.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Emitted when all chunks for an item have been uploaded and assembled in S3.
 */
@Value
@Builder
public class UploadCompletedEvent implements DomainEvent {
    UUID userId;
    UUID uploadSessionId;
    UUID itemId;
    String s3Key;
    String mimeType;
    long totalSize;
}
