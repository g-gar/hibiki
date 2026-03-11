package com.ggar.hibiki.features.metadata.model;

import java.util.UUID;
import lombok.Value;

/**
 * Value object representing a media item identity in the metadata context.
 */
@Value(staticConstructor = "of")
public class MediaId {
    UUID id;

    public static MediaId fromString(String uuid) {
        return new MediaId(UUID.fromString(uuid));
    }
}
