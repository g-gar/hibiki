package com.ggar.hibiki.features.ingestion.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import lombok.Builder;
import lombok.Value;

/**
 * Emitted when an upload is cancelled. Allows the quotas module
 * to revert any reserved storage space.
 */
@Value
@Builder
public class UploadCancelledEvent implements DomainEvent {
    UserId userId;
    UploadSessionId uploadSessionId;
}
