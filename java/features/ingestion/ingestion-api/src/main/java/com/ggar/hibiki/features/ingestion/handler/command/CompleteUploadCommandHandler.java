package com.ggar.hibiki.features.ingestion.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.dto.UploadSessionDto;
import com.ggar.hibiki.features.ingestion.model.UploadItemId;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import java.util.UUID;

/**
 * Interface for the handler responsible for completing an upload session.
 */
public interface CompleteUploadCommandHandler
        extends CommandHandler<CompleteUploadCommandHandler.Complete, UploadSessionDto> {

    /**
     * Command to complete an upload session.
     */
    record Complete(UUID userId, UUID uploadSessionId, String mimeType, String contentHash)
            implements Command<UploadSessionDto> {}

    /**
     * Event published when an upload session is successfully completed.
     */
    record Completed(
            UserId userId,
            UploadSessionId uploadSessionId,
            UploadItemId itemId,
            String s3Key,
            String mimeType,
            long totalSize)
            implements DomainEvent {}
}
