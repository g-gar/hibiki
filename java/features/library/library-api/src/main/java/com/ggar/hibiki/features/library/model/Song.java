package com.ggar.hibiki.features.library.model;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Lightweight domain representation of a song for the library context.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
@With
public class Song {
    /**
     * Unique identifier for the song in the catalog.
     */
    SongId id;
    /**
     * The album this song belongs to.
     */
    Album album;
    /**
     * The artists who performed the song.
     */
    List<Artist> artists;
}
