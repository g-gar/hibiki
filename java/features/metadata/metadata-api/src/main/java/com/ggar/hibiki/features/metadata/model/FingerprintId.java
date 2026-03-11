package com.ggar.hibiki.features.metadata.model;

import java.util.UUID;
import lombok.Value;

/**
 * Value object representing a unique acoustId fingerprint.
 */
@Value(staticConstructor = "of")
public class FingerprintId {
    String value;

    public static FingerprintId generate() {
        return new FingerprintId(UUID.randomUUID().toString());
    }
}
