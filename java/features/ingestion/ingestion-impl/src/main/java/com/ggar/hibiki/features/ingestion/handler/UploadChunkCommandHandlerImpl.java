package com.ggar.hibiki.features.ingestion.handler;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.ingestion.dto.UploadChunkCommand;
import com.ggar.hibiki.features.ingestion.event.ChunkUploadedEvent;
import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import com.ggar.hibiki.features.ingestion.model.UploadItem;
import com.ggar.hibiki.features.ingestion.model.UploadProgress;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.port.ContentHasher;
import com.ggar.hibiki.features.ingestion.port.MediaStorage;
import com.ggar.hibiki.features.ingestion.port.MediaTypeResolver;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import com.ggar.hibiki.features.ingestion.service.UploadChunkCommandHandler;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadChunkCommandHandlerImpl implements UploadChunkCommandHandler {

    private final UploadSessionRepository uploadSessionRepository;
    private final MediaStorage mediaStorage;
    private final MediaTypeResolver mediaTypeResolver;
    private final ContentHasher contentHasher;
    private final EventBus eventBus;

    @Override
    public Publisher<UploadProgress> handle(UploadChunkCommand command) {
        return uploadSessionRepository.findById(command.getUploadSessionId()).flatMap(session -> {
            UploadItem item = session.getItems().stream()
                    .filter(i -> i.getId().equals(command.getItemId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Item not found: " + command.getItemId()));

            if (session.getPhase() == IngestionPhase.CANCELLED) {
                return Mono.error(new IllegalStateException("Upload session is cancelled"));
            }

            // Collect chunk bytes
            return DataBufferUtils.join(command.getContent()).flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);

                // MIME type detection on first chunk
                Mono<String> mimeTypeMono;
                if (command.getChunkIndex() == 0) {
                    mimeTypeMono = mediaTypeResolver.resolve(bytes, item.getOriginalFilename());
                } else {
                    mimeTypeMono = Mono.justOrEmpty(item.getMimeType());
                }

                return mimeTypeMono.flatMap(mimeType -> {
                    // Progressive hashing
                    ContentHasher.HashState hashState;
                    if (command.getChunkIndex() == 0) {
                        hashState = contentHasher.init();
                    } else {
                        // In production, HashState would be retrieved from session state
                        hashState = contentHasher.init();
                    }
                    hashState = contentHasher.update(hashState, bytes);
                    String accumulatedHash = contentHasher.finalize(hashState);

                    // Upload part to S3
                    return mediaStorage
                            .uploadPart(
                                    item.getId().toString(),
                                    session.getId().toString(),
                                    command.getChunkIndex() + 1,
                                    bytes)
                            .flatMap(etag -> {
                                // Update item state
                                UploadItem updatedItem = item.withReceivedChunks(item.getReceivedChunks() + 1)
                                        .withMimeType(mimeType)
                                        .withAccumulatedHash(accumulatedHash)
                                        .withPhase(IngestionPhase.UPLOADING);

                                List<UploadItem> updatedItems = new ArrayList<>(session.getItems());
                                int idx = updatedItems.indexOf(item);
                                updatedItems.set(idx, updatedItem);

                                UploadSession updatedSession =
                                        session.withItems(updatedItems).withPhase(IngestionPhase.UPLOADING);

                                return uploadSessionRepository
                                        .save(updatedSession)
                                        .flatMap(s -> {
                                            var userId = command.getIdentityContext()
                                                    .getUser()
                                                    .getId();
                                            return eventBus.publish(ChunkUploadedEvent.builder()
                                                            .userId(userId)
                                                            .uploadSessionId(session.getId())
                                                            .itemId(item.getId())
                                                            .chunkIndex(command.getChunkIndex())
                                                            .chunkSize(bytes.length)
                                                            .accumulatedHash(accumulatedHash)
                                                            .build())
                                                    .onErrorResume(e -> {
                                                        log.error("Failed to publish ChunkUploadedEvent", e);
                                                        return Mono.empty();
                                                    })
                                                    .thenReturn(s);
                                        })
                                        .map(s -> {
                                            int progress = (int) ((double) updatedItem.getReceivedChunks()
                                                    / updatedItem.getTotalChunks()
                                                    * 100);
                                            return UploadProgress.builder()
                                                    .uploadSessionId(session.getId())
                                                    .itemId(item.getId())
                                                    .phase(updatedItem.getPhase())
                                                    .progress(progress)
                                                    .build();
                                        });
                            });
                });
            });
        });
    }
}
