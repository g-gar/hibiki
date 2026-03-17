package com.ggar.hibiki.features.history.handler.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Interface for the handler responsible for retrieving playback history.
 */
public interface GetPlaybackHistoryQueryHandler
        extends QueryHandler<GetPlaybackHistoryQueryHandler.Get, List<PlaybackHistoryEntry>> {

    /**
     * Query to retrieve playback history with optional filters and pagination.
     */
    record Get(
            UUID userId,
            UUID songId,
            UUID artistId,
            UUID albumId,
            UUID playlistId,
            UUID deviceId,
            Instant fromDate,
            Instant toDate,
            Integer page,
            Integer size)
            implements Query<List<PlaybackHistoryEntry>> {}
}
