package com.ggar.hibiki.features.history.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Domain entity representing a single playback event in the user's history.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaybackHistoryEntry {
    UUID id;
    IdentityContext identityContext;
    UUID songId;
    PlaybackContext playbackContext;
    Instant playedAt;
}
