package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.DeviceType;
import com.ggar.hibiki.features.devices.model.IdentityContext;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Command to register a new device for a user.
 */
@Value
@Builder(toBuilder = true)
@With
public class RegisterDeviceCommand implements Command<Device> {
    /**
     * Unique identifier for the device (optional, generated if null).
     */
    DeviceId id;
    /**
     * User identity context.
     */
    IdentityContext identityContext;
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
