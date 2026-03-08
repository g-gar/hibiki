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
 * Domain representation of a user within the library module.
 * Helps in defining clean relationships between library items, playlists, and users.
 */
@Data
@Builder(toBuilder = true)
@With
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    /**
     * Unique identifier for the user.
     */
    UUID id;
}
