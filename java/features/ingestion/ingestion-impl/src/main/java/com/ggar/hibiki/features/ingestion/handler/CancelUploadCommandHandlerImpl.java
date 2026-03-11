package com.ggar.hibiki.features.ingestion.handler;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.ingestion.dto.CancelUploadCommand;
import com.ggar.hibiki.features.ingestion.dto.UploadSessionDto;
import com.ggar.hibiki.features.ingestion.event.UploadCancelledEvent;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper.MediaMapper;
import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import com.ggar.hibiki.features.ingestion.port.MediaStorage;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import com.ggar.hibiki.features.ingestion.service.CancelUploadCommandHandler;
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
    private final MediaMapper mediaMapper;
    private final EventBus eventBus;

    @Override
    public Publisher<UploadSessionDto> handle(CancelUploadCommand command) {
        var userId = UserId.of(command.getUserId());

        return uploadSessionRepository
                .findById(UploadSessionId.of(command.getUploadSessionId()))
                .flatMap(session -> {
                    log.info("Cancelling upload session {} for user {}", session.getId(), userId);

                    return Flux.fromIterable(session.getItems())
                            .flatMap(item -> mediaStorage.abortMultipartUpload(
                                    item.getId().getId().toString(),
                                    session.getId().getId().toString()))
                            .then(Mono.from(uploadSessionRepository.save(session.withPhase(IngestionPhase.CANCELLED))))
                            .flatMap(s -> eventBus.publish(UploadCancelledEvent.builder()
                                            .userId(userId)
                                            .uploadSessionId(session.getId())
                                            .build())
                                    .onErrorResume(e -> {
                                        log.error("Failed to publish UploadCancelledEvent", e);
                                        return Mono.empty();
                                    })
                                    .thenReturn(mediaMapper.toDto(s)));
                });
    }
}
