package com.ggar.hibiki.features.devices.handler.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import java.util.UUID;

/**
 * Interface for the handler responsible for validating device login attempts.
 */
public interface ValidateDeviceLoginQueryHandler
        extends QueryHandler<ValidateDeviceLoginQueryHandler.Validate, Boolean> {

    /**
     * Query to validate if a device login is authorized for a specific user.
     */
    record Validate(UUID userId, UUID deviceId, String ip, String userAgent) implements Query<Boolean> {}
}
