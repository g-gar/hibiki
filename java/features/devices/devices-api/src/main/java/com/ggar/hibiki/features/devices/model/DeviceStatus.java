package com.ggar.hibiki.features.devices.model;

/**
 * Status of a device registration.
 */
public enum DeviceStatus {
    /**
     * The device is active and can be used for authentication.
     */
    ACTIVE,
    /**
     * The device registration has been revoked and cannot be used.
     */
    REVOKED
}
