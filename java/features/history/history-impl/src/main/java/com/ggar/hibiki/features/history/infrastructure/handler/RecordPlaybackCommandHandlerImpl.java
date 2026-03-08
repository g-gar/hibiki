package com.ggar.hibiki.features.history.infrastructure.handler;

import com.ggar.hibiki.features.history.dto.RecordPlaybackCommand;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import com.ggar.hibiki.features.history.port.PlaybackHistoryRepository;
import com.ggar.hibiki.features.history.service.RecordPlaybackCommandHandler;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link RecordPlaybackCommandHandler}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecordPlaybackCommandHandlerImpl implements RecordPlaybackCommandHandler {

    private final PlaybackHistoryRepository repository;

    @Override
    public Mono<PlaybackHistoryEntry> handle(RecordPlaybackCommand command) {
        log.debug(
                "Recording playback for user: {}, song: {}",
                command.getIdentityContext().getUserId(),
                command.getSongId());

        PlaybackHistoryEntry entry = PlaybackHistoryEntry.builder()
                .id(UUID.randomUUID())
                .identityContext(command.getIdentityContext())
                .songId(command.getSongId())
                .playbackContext(command.getPlaybackContext())
                .playedAt(Instant.now())
                .build();

        return repository.save(entry).doOnSuccess(saved -> log.info("Playback recorded with id: {}", saved.getId()));
    }
}
