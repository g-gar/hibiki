package com.ggar.hibiki.features.ingestion.handler;

import com.ggar.hibiki.features.ingestion.dto.InitiateUploadCommand;
import com.ggar.hibiki.features.ingestion.dto.ItemDescriptor;
import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import com.ggar.hibiki.features.ingestion.model.UploadItem;
import com.ggar.hibiki.features.ingestion.model.UploadItemId;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.User;
import com.ggar.hibiki.features.ingestion.port.MediaStorage;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import com.ggar.hibiki.features.ingestion.service.InitiateUploadCommandHandler;
import com.ggar.hibiki.packages.uuid.UuidV7Generator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiateUploadCommandHandlerImpl implements InitiateUploadCommandHandler {

    private static final long DEFAULT_CHUNK_SIZE = 5 * 1024 * 1024; // 5MB

    private final UploadSessionRepository uploadSessionRepository;
    private final MediaStorage mediaStorage;
    private final UuidV7Generator idGenerator;

    @Override
    public Publisher<UploadSession> handle(InitiateUploadCommand command) {
        var sessionId = idGenerator.generate();
        var userId = command.getIdentityContext().getUser().getId();

        log.info("Initiating upload session {} for user {}", sessionId, userId);

        List<UploadItem> items = new ArrayList<>();
        for (ItemDescriptor descriptor : command.getItems()) {
            int totalChunks = (int) Math.ceil((double) descriptor.getExpectedSize() / DEFAULT_CHUNK_SIZE);
            items.add(UploadItem.builder()
                    .id(new UploadItemId(idGenerator.generate()))
                    .originalFilename(descriptor.getOriginalFilename())
                    .expectedSize(descriptor.getExpectedSize())
                    .totalChunks(totalChunks)
                    .receivedChunks(0)
                    .phase(IngestionPhase.INITIATED)
                    .build());
        }

        UploadSession session = UploadSession.builder()
                .id(new UploadSessionId(sessionId))
                .userId(new User(userId))
                .items(items)
                .phase(IngestionPhase.INITIATED)
                .createdAt(Instant.now())
                .build();

        return Flux.fromIterable(items)
                .flatMap(item -> mediaStorage.initiateMultipartUpload(
                        item.getId().getId().toString()))
                .then(uploadSessionRepository.save(session))
                .doOnSuccess(saved -> log.info("Upload session {} created with {} items", sessionId, items.size()));
    }
}
