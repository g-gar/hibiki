package com.ggar.hibiki.features.history.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.history.model.ContextType;
import com.ggar.hibiki.features.history.model.DeviceId;
import com.ggar.hibiki.features.history.model.IdentityContext;
import com.ggar.hibiki.features.history.model.PlaybackContext;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import com.ggar.hibiki.features.history.model.PlaybackHistoryId;
import com.ggar.hibiki.features.history.model.SessionId;
import com.ggar.hibiki.features.history.model.SongId;
import com.ggar.hibiki.features.history.model.UserId;
import com.ggar.hibiki.features.history.port.PlaybackHistoryRepository;
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
    private final EventBus eventBus;

    @Override
    public Mono<PlaybackHistoryEntry> handle(RecordPlaybackCommandHandler.Record command) {
        log.debug("Recording playback for user: {}, song: {}", command.userId(), command.songId());

        IdentityContext identityContext = IdentityContext.builder()
                .userId(command.userId() != null ? UserId.of(command.userId()) : null)
                .sessionId(command.sessionId() != null ? SessionId.of(command.sessionId()) : null)
                .deviceId(command.deviceId() != null ? DeviceId.of(command.deviceId()) : null)
                .build();

        PlaybackContext playbackContext = command.contextType() != null
                ? PlaybackContext.builder()
                        .type(ContextType.valueOf(command.contextType()))
                        .id(command.contextId())
                        .build()
                : null;

        PlaybackHistoryEntry entry = PlaybackHistoryEntry.builder()
                .id(PlaybackHistoryId.of(UUID.randomUUID()))
                .identityContext(identityContext)
                .songId(command.songId() != null ? SongId.of(command.songId()) : null)
                .playbackContext(playbackContext)
                .playedAt(Instant.now())
                .build();

        return repository.save(entry).doOnSuccess(saved -> {
            log.info("Playback recorded with id: {}", saved.getId().getValue());
            eventBus.publish(new RecordPlaybackCommandHandler.Recorded(
                    saved.getId().getValue(),
                    saved.getIdentityContext().getUserId().getValue(),
                    saved.getSongId().getValue()));
        });
    }
}
