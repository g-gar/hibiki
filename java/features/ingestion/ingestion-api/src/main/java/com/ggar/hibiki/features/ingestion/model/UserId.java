package com.ggar.hibiki.features.ingestion.model;

import java.util.UUID;
import lombok.Value;

/**
 * Identity reference for a user. Lightweight wrapper for cross-aggregate references
 * (events, pipeline context) where the full User object is not needed.
 */
@Value
public class UserId {
    UUID id;
}
