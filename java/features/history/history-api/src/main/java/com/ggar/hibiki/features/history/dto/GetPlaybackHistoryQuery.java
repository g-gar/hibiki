package com.ggar.hibiki.features.history.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Query to retrieve playback history with optional filters and pagination.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetPlaybackHistoryQuery implements Query<PlaybackHistoryEntry> {
    UUID userId;
    UUID songId;
    UUID artistId;
    UUID albumId;
    UUID playlistId;
    UUID deviceId;
    Instant fromDate;
    Instant toDate;

    @Builder.Default
    Integer page = 0;

    @Builder.Default
    Integer size = 20;
}
