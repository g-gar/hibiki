package com.ggar.hibiki.features.library.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Context information identifying the user for library operations.
 */
@Value
@Builder(toBuilder = true)
@With
public class IdentityContext {
    /**
     * The user context for library operations.
     */
    User user;
}
