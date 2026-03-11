package com.ggar.hibiki.features.library.model;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Value object representing a unique identifier for a library item.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@Builder(toBuilder = true)
public class LibraryItemId {
    UUID value;
}
