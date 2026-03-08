package com.ggar.hibiki.features.library.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import java.util.UUID;
import lombok.Value;

/**
 * Event emitted when a media item is removed from a user's library.
 */
@Value
public class MediaRemovedFromLibraryEvent implements DomainEvent {
    UUID userId;
    UUID mediaId;
}
