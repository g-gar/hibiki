package com.ggar.hibiki.features.library.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * Base abstract class for items saved in the user's library.
 * Uses inheritance to distinguish between different types of media.
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class LibraryItem {
    /**
     * Unique identifier for this library entry.
     */
    UUID id;
    /**
     * The user who owns this library entry.
     */
    User user;
    /**
     * Visibility of this item in the user's library.
     */
    Visibility visibility;
    /**
     * Whether the user is the original owner/creator of this item.
     */
    boolean owner;
    /**
     * When the item was added to the library.
     */
    Instant addedAt;

    /**
     * Gets the type of the media item.
     */
    public abstract LibraryItemType getType();
}
