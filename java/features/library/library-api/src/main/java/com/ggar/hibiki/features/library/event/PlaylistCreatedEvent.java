package com.ggar.hibiki.features.library.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.features.library.model.UserId;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Event emitted when a playlist is created.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class PlaylistCreatedEvent implements DomainEvent {
    UserId userId;
    UUID playlistId;
    String name;
}
