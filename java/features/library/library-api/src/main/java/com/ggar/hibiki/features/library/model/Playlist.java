package com.ggar.hibiki.features.library.model;

import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

/**
 * Represents a user-defined collection of songs, which is also a library item.
 */
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class Playlist extends LibraryItem {
    /**
     * User-defined name for the playlist.
     */
    String name;
    /**
     * Optional description of the playlist.
     */
    String description;
    /**
     * Ordered list of items within the playlist.
     */
    List<PlaylistItem> items;
    /**
     * When the playlist was last updated.
     */
    Instant updatedAt;

    @Override
    public LibraryItemType getType() {
        return LibraryItemType.PLAYLIST;
    }
}
