package com.ggar.hibiki.features.metadata.model;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Value object representing a media item identity in the metadata context.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@Builder(toBuilder = true)
public class MediaId {
    UUID id;

    public static MediaId fromString(String uuid) {
        return new MediaId(UUID.fromString(uuid));
    }
}
