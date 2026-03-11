package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.ingestion.model.UploadProgress;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Value;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

/**
 * Uploads a single chunk of an item. On the first chunk (chunkIndex == 0),
 * MIME type is detected inline via MediaTypeResolver.
 */
@Value
@Builder
public class UploadChunkCommand implements Command<UploadProgress> {
    UUID userId;
    UUID uploadSessionId;
    UUID itemId;
    int chunkIndex;
    Flux<DataBuffer> content;

    Runnable onUploadStarted;
    Runnable onUploadCompleted;
    Consumer<byte[]> onChunkProcessed;
}
