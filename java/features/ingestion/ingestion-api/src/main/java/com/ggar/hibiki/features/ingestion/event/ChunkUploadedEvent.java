package com.ggar.hibiki.features.ingestion.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.features.ingestion.model.UploadItemId;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import lombok.Builder;
import lombok.Value;

/**
 * Emitted after each chunk is uploaded. Consumed by the quotas module
 * and potentially by a deduplication module for early duplicate detection.
 */
@Value
@Builder
public class ChunkUploadedEvent implements DomainEvent {
    UserId userId;
    UploadSessionId uploadSessionId;
    UploadItemId itemId;
    int chunkIndex;
    long chunkSize;
    String accumulatedHash;
}
