package com.ggar.hibiki.features.devices.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Context information identifying the user for device operations.
 */
@Value
@Builder(toBuilder = true)
@With
public class IdentityContext {
    /**
     * The user context for device operations.
     */
    User user;
}
