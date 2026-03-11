package com.ggar.hibiki.features.history.infrastructure.handler;

import com.ggar.hibiki.features.history.dto.RecordPlaybackCommand;
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
        log.debug("Recording playback for user: {}, song: {}", command.getUserId(), command.getSongId());

        IdentityContext identityContext = IdentityContext.builder()
                .userId(command.getUserId() != null ? UserId.of(command.getUserId()) : null)
                .sessionId(command.getSessionId() != null ? SessionId.of(command.getSessionId()) : null)
                .deviceId(command.getDeviceId() != null ? DeviceId.of(command.getDeviceId()) : null)
                .build();

        PlaybackContext playbackContext = command.getContextType() != null
                ? PlaybackContext.builder()
                        .type(ContextType.valueOf(command.getContextType()))
                        .id(command.getContextId())
                        .build()
                : null;

        PlaybackHistoryEntry entry = PlaybackHistoryEntry.builder()
                .id(PlaybackHistoryId.of(UUID.randomUUID()))
                .identityContext(identityContext)
                .songId(command.getSongId() != null ? SongId.of(command.getSongId()) : null)
                .playbackContext(playbackContext)
                .playedAt(Instant.now())
                .build();

        return repository
                .save(entry)
                .doOnSuccess(saved ->
                        log.info("Playback recorded with id: {}", saved.getId().getValue()));
    }
}
