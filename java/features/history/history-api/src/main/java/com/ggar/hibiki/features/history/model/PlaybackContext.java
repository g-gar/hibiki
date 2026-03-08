package com.ggar.hibiki.features.history.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Represents the specific collection or entity (e.g., Album, Playlist) associated with a playback.
 */
@Value
@Builder
public class PlaybackContext {
    ContextType type;
    UUID id;
}
