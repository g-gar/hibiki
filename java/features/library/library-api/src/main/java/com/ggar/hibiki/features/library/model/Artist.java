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
 * Lightweight domain representation of an artist for the library context.
 */
@Data
@Builder(toBuilder = true)
@With
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Artist {
    /**
     * Unique identifier for the artist in the catalog.
     */
    UUID id;
}
