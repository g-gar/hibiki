package com.ggar.hibiki.features.history.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Context information identifying the user, session, and device during a playback event.
 */
@Value
@Builder
public class IdentityContext {
    UUID userId;
    String sessionId;
    UUID deviceId;
}
