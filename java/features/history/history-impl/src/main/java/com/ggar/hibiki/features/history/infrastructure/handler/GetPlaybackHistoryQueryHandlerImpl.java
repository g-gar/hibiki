package com.ggar.hibiki.features.history.infrastructure.handler;

import com.ggar.hibiki.features.history.dto.GetPlaybackHistoryQuery;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import com.ggar.hibiki.features.history.port.PlaybackHistoryRepository;
import com.ggar.hibiki.features.history.service.GetPlaybackHistoryQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link GetPlaybackHistoryQueryHandler}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetPlaybackHistoryQueryHandlerImpl implements GetPlaybackHistoryQueryHandler {

    private final PlaybackHistoryRepository repository;

    @Override
    public Mono<Flux<PlaybackHistoryEntry>> handle(GetPlaybackHistoryQuery query) {
        log.debug(
                "Retrieving playback history for user: {}, page: {}, size: {}",
                query.getUserId(),
                query.getPage(),
                query.getSize());

        if (query.getUserId() == null) {
            log.warn("Attempted to retrieve history with null userId");
            return Mono.just(Flux.empty());
        }
        return Mono.just(repository
                .findByUserId(query.getUserId())
                .skip((long) query.getPage() * query.getSize())
                .take(query.getSize()));
    }
}
