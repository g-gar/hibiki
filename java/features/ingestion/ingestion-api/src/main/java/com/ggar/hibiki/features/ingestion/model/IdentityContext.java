package com.ggar.hibiki.features.ingestion.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Context information identifying the user for ingestion operations.
 */
@Value
@Builder(toBuilder = true)
@With
public class IdentityContext {
    /**
     * The user context for ingestion operations.
     */
    User user;
}
