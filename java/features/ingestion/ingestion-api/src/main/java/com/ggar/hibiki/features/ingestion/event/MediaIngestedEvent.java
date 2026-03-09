package com.ggar.hibiki.features.ingestion.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Emitted after the ingestion pipeline completes for a media item.
 * Consumed by orchestrator, library, catalog, deduplication, and other modules.
 */
@Value
@Builder
public class MediaIngestedEvent implements DomainEvent {
    UUID userId;
    UUID mediaId;
    String s3Key;
    String mimeType;
    String contentHash;
}
