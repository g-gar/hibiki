package com.ggar.hibiki.features.ingestion.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.features.ingestion.model.UploadItemId;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import lombok.Builder;
import lombok.Value;

/**
 * Emitted when all chunks for an item have been uploaded and assembled in S3.
 */
@Value
@Builder
public class UploadCompletedEvent implements DomainEvent {
    UserId userId;
    UploadSessionId uploadSessionId;
    UploadItemId itemId;
    String s3Key;
    String mimeType;
    long totalSize;
}
