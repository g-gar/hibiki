package com.ggar.hibiki.features.devices.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Domain representation of a user within the devices module.
 * Helps in defining clean relationships between devices and users.
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
    UserId id;
}
