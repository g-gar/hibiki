package com.ggar.hibiki.features.library.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import java.util.UUID;
import lombok.Value;

/**
 * Event emitted when a playlist is updated.
 */
@Value
public class PlaylistUpdatedEvent implements DomainEvent {
    UUID userId;
    UUID playlistId;
}
