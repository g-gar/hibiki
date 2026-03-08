package com.ggar.hibiki.features.library.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Represents an entry within a playlist, usually a song from the catalog.
 */
@Data
@Builder(toBuilder = true)
@With
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaylistItem {
    /**
     * Unique identifier for this playlist item entry.
     */
    UUID id;
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
