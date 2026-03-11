package com.ggar.hibiki.features.library.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Lightweight domain representation of an artist for the library context.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
@With
public class Artist {
    /**
     * Unique identifier for the artist in the catalog.
     */
    ArtistId id;
}
