package com.ggar.hibiki.features.ingestion.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Emitted after each chunk is uploaded. Consumed by the quotas module
 * and potentially by a deduplication module for early duplicate detection.
 */
@Value
@Builder
public class ChunkUploadedEvent implements DomainEvent {
    UUID userId;
    UUID uploadSessionId;
    UUID itemId;
    int chunkIndex;
    long chunkSize;
    String accumulatedHash;
}
