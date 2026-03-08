package com.ggar.hibiki.features.history.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

/**
 * Query to retrieve playback history with optional filters and pagination.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPlaybackHistoryQuery implements Query<Flux<PlaybackHistoryEntry>> {
    private UUID userId;
    private UUID songId;
    private UUID artistId;
    private UUID albumId;
    private UUID playlistId;
    private String deviceId;
    private Instant fromDate;
    private Instant toDate;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 20;
}
