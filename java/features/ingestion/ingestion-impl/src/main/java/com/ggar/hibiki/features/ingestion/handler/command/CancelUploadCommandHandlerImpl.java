package com.ggar.hibiki.features.ingestion.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import com.ggar.hibiki.features.ingestion.port.MediaStorage;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelUploadCommandHandlerImpl implements CancelUploadCommandHandler {

    private final UploadSessionRepository uploadSessionRepository;
    private final MediaStorage mediaStorage;
    private final EventBus eventBus;

    @Override
    public Publisher<UploadSession> handle(CancelUploadCommandHandler.Cancel command) {
        var userId = UserId.of(command.userId());

        return uploadSessionRepository
                .findById(UploadSessionId.of(command.uploadSessionId()))
                .flatMap(session -> {
                    log.info("Cancelling upload session {} for user {}", session.getId(), userId);

                    return Flux.fromIterable(session.getItems())
                            .flatMap(item -> mediaStorage.abortMultipartUpload(
                                    item.getId().getId().toString(),
                                    session.getId().getId().toString()))
                            .then(Mono.from(uploadSessionRepository.save(session.withPhase(IngestionPhase.CANCELLED))))
                            .flatMap(s -> eventBus.publish(
                                            new CancelUploadCommandHandler.Cancelled(userId, session.getId()))
                                    .onErrorResume(e -> {
                                        log.error("Failed to publish Cancelled event", e);
                                        return Mono.empty();
                                    })
                                    .thenReturn(s));
                });
    }
}
