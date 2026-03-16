package com.ggar.hibiki.features.ingestion.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.dto.UploadProgressDto;
import com.ggar.hibiki.features.ingestion.model.UploadItemId;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import java.util.UUID;
import java.util.function.Consumer;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

/**
 * Interface for the handler responsible for uploading a single chunk of an item.
 */
public interface UploadChunkCommandHandler extends CommandHandler<UploadChunkCommandHandler.Upload, UploadProgressDto> {

    /**
     * Command to upload a single chunk of an item.
     */
    record Upload(
            UUID userId,
            UUID uploadSessionId,
            UUID itemId,
            int chunkIndex,
            Flux<DataBuffer> content,
            Runnable onUploadStarted,
            Runnable onUploadCompleted,
            Consumer<byte[]> onChunkProcessed)
            implements Command<UploadProgressDto> {}

    /**
     * Event published after each chunk is successfully uploaded.
     */
    record ChunkUploaded(
            UserId userId,
            UploadSessionId uploadSessionId,
            UploadItemId itemId,
            int chunkIndex,
            long chunkSize,
            String accumulatedHash)
            implements DomainEvent {}
}
