package com.ggar.hibiki.features.library.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Lightweight domain representation of an album for the library context.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
@With
public class Album {
    /**
     * Unique identifier for the album in the catalog.
     */
    AlbumId id;
    /**
     * The primary artist of the album.
     */
    Artist artist;
}
