package com.ggar.hibiki.features.library.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import java.util.UUID;
import lombok.Value;

/**
 * Event emitted when a playlist is deleted.
 */
@Value
public class PlaylistDeletedEvent implements DomainEvent {
    UUID userId;
    UUID playlistId;
}
