package com.ggar.hibiki.features.history.handler.query;

import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import com.ggar.hibiki.features.history.model.UserId;
import com.ggar.hibiki.features.history.port.PlaybackHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    public Mono<List<PlaybackHistoryEntry>> handle(GetPlaybackHistoryQueryHandler.Get query) {
        log.debug(
                "Retrieving playback history for user: {}, page: {}, size: {}",
                query.userId(),
                query.page(),
                query.size());

        if (query.userId() == null) {
            log.warn("Attempted to retrieve history with null userId");
            return Mono.just(List.of());
        }

        int page = query.page() != null ? query.page() : 0;
        int size = query.size() != null ? query.size() : 20;

        return repository
                .findByUserId(UserId.of(query.userId()))
                .skip((long) page * size)
                .take(size)
                .collectList();
    }
}
