package com.ggar.hibiki.features.history.service;

import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.history.dto.GetPlaybackHistoryQuery;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import reactor.core.publisher.Flux;

/**
 * Inbound service for handling playback history retrieval queries.
 */
public interface GetPlaybackHistoryQueryHandler
        extends QueryHandler<GetPlaybackHistoryQuery, Flux<PlaybackHistoryEntry>> {}
