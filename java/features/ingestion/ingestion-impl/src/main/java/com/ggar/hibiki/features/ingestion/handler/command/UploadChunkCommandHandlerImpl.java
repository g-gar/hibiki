package com.ggar.hibiki.features.ingestion.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.ingestion.dto.UploadProgressDto;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper.MediaMapper;
import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import com.ggar.hibiki.features.ingestion.model.UploadItem;
import com.ggar.hibiki.features.ingestion.model.UploadItemId;
import com.ggar.hibiki.features.ingestion.model.UploadProgress;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import com.ggar.hibiki.features.ingestion.port.MediaStorage;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
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
    private final MediaMapper mediaMapper;
    private final EventBus eventBus;

    @Override
    public Publisher<UploadProgressDto> handle(UploadChunkCommandHandler.Upload command) {
        return uploadSessionRepository
                .findById(UploadSessionId.of(command.uploadSessionId()))
                .flatMap(session -> {
                    UploadItem item = session.getItems().stream()
                            .filter(i -> i.getId().equals(UploadItemId.of(command.itemId())))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Item not found: " + command.itemId()));

                    if (session.getPhase() == IngestionPhase.CANCELLED) {
                        return Mono.error(new IllegalStateException("Upload session is cancelled"));
                    }

                    if (command.onUploadStarted() != null) {
                        command.onUploadStarted().run();
                    }

                    // Collect chunk bytes
                    return DataBufferUtils.join(command.content()).flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);

                        // Release buffer as we don't need it internally beyond this point
                        DataBufferUtils.release(dataBuffer);

                        if (command.onChunkProcessed() != null) {
                            command.onChunkProcessed().accept(bytes);
                        }

                        // Upload part to S3
                        return mediaStorage
                                .uploadPart(
                                        item.getId().toString(),
                                        session.getId().toString(),
                                        command.chunkIndex() + 1,
                                        bytes)
                                .flatMap(etag -> {
                                    // Update item state
                                    UploadItem updatedItem = item.withReceivedChunks(item.getReceivedChunks() + 1)
                                            .withPhase(IngestionPhase.UPLOADING);

                                    List<UploadItem> updatedItems = new ArrayList<>(session.getItems());
                                    int idx = updatedItems.indexOf(item);
                                    updatedItems.set(idx, updatedItem);

                                    UploadSession updatedSession =
                                            session.withItems(updatedItems).withPhase(IngestionPhase.UPLOADING);

                                    return uploadSessionRepository
                                            .save(updatedSession)
                                            .flatMap(s -> {
                                                var userId = UserId.of(command.userId());
                                                return eventBus.publish(new UploadChunkCommandHandler.ChunkUploaded(
                                                                userId,
                                                                session.getId(),
                                                                item.getId(),
                                                                command.chunkIndex(),
                                                                bytes.length,
                                                                null))
                                                        .onErrorResume(e -> {
                                                            log.error("Failed to publish ChunkUploaded", e);
                                                            return Mono.empty();
                                                        })
                                                        .thenReturn(s);
                                            })
                                            .map(s -> {
                                                int progress = (int) ((double) updatedItem.getReceivedChunks()
                                                        / updatedItem.getTotalChunks()
                                                        * 100);
                                                return mediaMapper.toDto(UploadProgress.builder()
                                                        .uploadSessionId(
                                                                session.getId().getId())
                                                        .itemId(item.getId().getId())
                                                        .phase(updatedItem.getPhase())
                                                        .progress(progress)
                                                        .build());
                                            });
                                })
                                .doOnSuccess(progress -> {
                                    if (command.onUploadCompleted() != null) {
                                        command.onUploadCompleted().run();
                                    }
                                });
                    });
                });
    }
}
