package com.ggar.hibiki.features.library.model;

import java.util.UUID;
import lombok.Value;

/**
 * Value object representing a unique identifier for a song.
 */
@Value(staticConstructor = "of")
public class SongId {
    UUID value;

    @Override
    public String toString() {
        return value.toString();
    }
}
