package com.ggar.hibiki.features.library.event;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import java.util.UUID;
import lombok.Value;

/**
 * Event emitted when a media item (Song/Album) is added to a user's library.
 */
@Value
public class MediaAddedToLibraryEvent implements DomainEvent {
    UUID userId;
    UUID mediaId;
    LibraryItemType type;
}
