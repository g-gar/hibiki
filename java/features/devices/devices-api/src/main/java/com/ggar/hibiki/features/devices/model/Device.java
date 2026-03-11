package com.ggar.hibiki.features.devices.model;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Domain representation of a device registered in the system.
 */
@Value
@Builder(toBuilder = true)
@With
public class Device {
    /**
     * Unique identifier for the device.
     */
    DeviceId id;
    /**
     * Identifier of the user who owns the device.
     */
    UserId userId;
    /**
     * Human-readable name given to the device.
     */
    String name;
    /**
     * Type of device (e.g., WEB, MOBILE, DESKTOP).
     */
    DeviceType type;
    /**
     * Current status of the device registration (e.g., ACTIVE, REVOKED).
     */
    DeviceStatus status;
    /**
     * User agent string from the device's login.
     */
    String userAgent;
    /**
     * Last known IP address of the device.
     */
    String lastIp;
    /**
     * Timestamp of when the device was registered.
     */
    Instant createdAt;
    /**
     * Timestamp of when the device was last seen.
     */
    Instant lastSeenAt;
}
