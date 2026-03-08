package com.ggar.hibiki.features.library.model;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Lightweight domain representation of an album for the library context.
 */
@Data
@Builder(toBuilder = true)
@With
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Album {
    /**
     * Unique identifier for the album in the catalog.
     */
    UUID id;
    /**
     * The primary artist of the album.
     */
    Artist artist;
}
