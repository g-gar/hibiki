package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.IdentityContext;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Query to validate if a device login is authorized for a specific user.
 */
@Value
@Builder(toBuilder = true)
@With
public class ValidateDeviceLoginQuery implements Query<Boolean> {
    /**
     * User identity context.
     */
    IdentityContext identityContext;
    /**
     * Identifier of the device attempting to login.
     */
    DeviceId deviceId;
    /**
     * IP address of the login attempt.
     */
    String ip;
    /**
     * User agent of the login attempt.
     */
    String userAgent;
}
