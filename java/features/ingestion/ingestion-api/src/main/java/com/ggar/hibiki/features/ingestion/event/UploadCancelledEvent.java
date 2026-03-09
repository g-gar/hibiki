package com.ggar.hibiki.features.ingestion.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Emitted when an upload is cancelled. Allows the quotas module
 * to revert any reserved storage space.
 */
@Value
@Builder
public class UploadCancelledEvent implements DomainEvent {
    UUID userId;
    UUID uploadSessionId;
}
