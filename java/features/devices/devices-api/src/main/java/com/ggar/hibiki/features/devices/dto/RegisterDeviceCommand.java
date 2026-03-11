package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.DeviceType;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Command to register a new device for a user.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegisterDeviceCommand implements Command<Device> {
    /**
     * Unique identifier for the device (optional, generated if null).
     */
    UUID id;
    /**
     * User identity identifier.
     */
    UUID userId;
    /**
     * Human-readable name given to the device.
     */
    String name;
    /**
     * Type of device.
     */
    DeviceType type;
    /**
     * User agent string from the device.
     */
    String userAgent;
    /**
     * IP address of the device.
     */
    String ip;
}
