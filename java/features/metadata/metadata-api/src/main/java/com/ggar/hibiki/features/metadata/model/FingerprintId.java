package com.ggar.hibiki.features.metadata.model;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Value object representing a unique acoustId fingerprint.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@Builder(toBuilder = true)
public class FingerprintId {
    String value;

    public static FingerprintId generate() {
        return new FingerprintId(UUID.randomUUID().toString());
    }
}
