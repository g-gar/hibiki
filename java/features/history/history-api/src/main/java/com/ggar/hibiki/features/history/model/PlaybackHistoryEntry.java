package com.ggar.hibiki.features.history.model;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Domain entity representing a single playback event in the user's history.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class PlaybackHistoryEntry {
    PlaybackHistoryId id;
    IdentityContext identityContext;
    SongId songId;
    PlaybackContext playbackContext;
    Instant playedAt;
}
