package com.ggar.hibiki.features.ingestion.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.features.ingestion.model.MediaId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import lombok.Builder;
import lombok.Value;

/**
 * Emitted after the ingestion pipeline completes for a media item.
 * Consumed by orchestrator, library, catalog, deduplication, and other modules.
 */
@Value
@Builder
public class MediaIngestedEvent implements DomainEvent {
    UserId userId;
    MediaId mediaId;
    String s3Key;
    String mimeType;
    String contentHash;
}
