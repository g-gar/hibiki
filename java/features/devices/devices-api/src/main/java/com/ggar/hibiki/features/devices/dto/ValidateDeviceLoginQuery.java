package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Query to validate if a device login is authorized for a specific user.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class ValidateDeviceLoginQuery implements Query<Boolean> {
    /**
     * User identity identifier.
     */
    UUID userId;
    /**
     * Identifier of the device attempting to login.
     */
    UUID deviceId;
    /**
     * IP address of the login attempt.
     */
    String ip;
    /**
     * User agent of the login attempt.
     */
    String userAgent;
}
