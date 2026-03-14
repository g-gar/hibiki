package com.ggar.hibiki.features.ingestion.handler;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.ingestion.dto.CompleteUploadCommand;
import com.ggar.hibiki.features.ingestion.dto.UploadSessionDto;
import com.ggar.hibiki.features.ingestion.event.MediaIngestedEvent;
import com.ggar.hibiki.features.ingestion.event.UploadCompletedEvent;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper.MediaMapper;
import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import com.ggar.hibiki.features.ingestion.model.Media;
import com.ggar.hibiki.features.ingestion.model.MediaId;
import com.ggar.hibiki.features.ingestion.model.MediaStatus;
import com.ggar.hibiki.features.ingestion.model.UploadItem;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.User;
import com.ggar.hibiki.features.ingestion.model.UserId;
import com.ggar.hibiki.features.ingestion.pipeline.IngestionContext;
import com.ggar.hibiki.features.ingestion.pipeline.IngestionPipeline;
import com.ggar.hibiki.features.ingestion.port.MediaRepository;
import com.ggar.hibiki.features.ingestion.port.MediaStorage;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import com.ggar.hibiki.features.ingestion.service.CompleteUploadCommandHandler;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompleteUploadCommandHandlerImpl implements CompleteUploadCommandHandler {

    private final UploadSessionRepository uploadSessionRepository;
    private final MediaStorage mediaStorage;
    private final MediaRepository mediaRepository;
    private final IngestionPipeline ingestionPipeline;
    private final MediaMapper mediaMapper;
    private final EventBus eventBus;

    @Override
    public Publisher<UploadSessionDto> handle(CompleteUploadCommand command) {
        var userId = UserId.of(command.getUserId());

        return uploadSessionRepository
                .findById(UploadSessionId.of(command.getUploadSessionId()))
                .flatMap(session -> {
                    log.info("Completing upload session {} for user {}", session.getId(), userId);

                    return Flux.fromIterable(session.getItems())
                            .flatMap(item -> mediaStorage
                                    .completeMultipartUpload(
                                            item.getId().getId().toString(),
                                            session.getId().getId().toString(),
                                            List.of())
                                    .then(createAndPersistMedia(
                                            item, userId, command.getMimeType(), command.getContentHash()))
                                    .flatMap(media -> runPipelineAndPublishEvents(media, item, session, userId)))
                            .then(Mono.defer(() -> Mono.from(uploadSessionRepository.save(
                                    session.withPhase(IngestionPhase.COMPLETED).withCompletedAt(Instant.now())))))
                            .map(mediaMapper::toDto);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Upload session not found")));
    }

    private Mono<Media> createAndPersistMedia(UploadItem item, UserId userId, String mimeType, String contentHash) {
        Media media = Media.builder()
                .id(MediaId.of(item.getId().getId()))
                .filename(item.getOriginalFilename())
                .mimeType(mimeType)
                .status(MediaStatus.PENDING)
                .contentHash(contentHash)
                .uploadedAt(Instant.now())
                .uploadedBy(new User(userId))
                .build();

        return mediaRepository.save(media);
    }

    private Mono<Void> runPipelineAndPublishEvents(Media media, UploadItem item, UploadSession session, UserId userId) {

        IngestionContext context = IngestionContext.builder()
                .mediaId(media.getId())
                .s3Key(item.getId().getId().toString())
                .mimeType(media.getMimeType())
                .userId(userId)
                .currentPhase(IngestionPhase.PROCESSING)
                .build();

        return ingestionPipeline
                .execute(context)
                .flatMap(ctx -> eventBus.publish(UploadCompletedEvent.builder()
                                .userId(userId)
                                .uploadSessionId(session.getId())
                                .itemId(item.getId())
                                .s3Key(item.getId().getId().toString())
                                .mimeType(media.getMimeType())
                                .totalSize(item.getExpectedSize())
                                .build())
                        .onErrorResume(e -> {
                            log.error("Failed to publish UploadCompletedEvent", e);
                            return Mono.empty();
                        })
                        .then(eventBus.publish(MediaIngestedEvent.builder()
                                .userId(userId)
                                .mediaId(media.getId())
                                .s3Key(item.getId().getId().toString())
                                .mimeType(media.getMimeType())
                                .contentHash(media.getContentHash())
                                .build()))
                        .onErrorResume(e -> {
                            log.error("Failed to publish MediaIngestedEvent", e);
                            return Mono.empty();
                        })
                        .doOnTerminate(() -> log.info("Media {} ingested successfully", media.getId())))
                .then();
    }
}
