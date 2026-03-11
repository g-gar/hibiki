package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Command to revoke access for a specific device.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class RevokeDeviceCommand implements Command<Void> {
    /**
     * User identity identifier.
     */
    UUID userId;
    /**
     * Identifier of the device to revoke.
     */
    UUID deviceId;
}
