package com.ggar.hibiki.features.devices.model;

import java.util.UUID;
import lombok.Value;

/**
 * Value object representing a unique identifier for a user.
 */
@Value(staticConstructor = "of")
public class UserId {
    UUID value;

    @Override
    public String toString() {
        return value.toString();
    }
}
