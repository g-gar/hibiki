package com.ggar.hibiki.features.library.model;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Lightweight domain representation of a song for the library context.
 */
@Data
@Builder(toBuilder = true)
@With
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
