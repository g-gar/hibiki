package com.ggar.hibiki.features.library.model;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Represents an entry within a playlist, usually a song from the catalog.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
@With
public class PlaylistItem {
    /**
     * Unique identifier for this playlist item entry.
     */
    PlaylistItemId id;
    /**
     * The referenced song in the catalog.
     */
    Song song;
    /**
     * Zero-based position of the song in the playlist.
     */
    Integer position;
    /**
     * When the song was added to the playlist.
     */
    Instant addedAt;
}
