package com.ggar.hibiki.features.ingestion.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.dto.UploadSessionDto;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import java.util.UUID;

/**
 * Interface for the handler responsible for cancelling an upload session.
 */
public interface CancelUploadCommandHandler
        extends CommandHandler<CancelUploadCommandHandler.Cancel, UploadSessionDto> {

    /**
     * Command to cancel an active upload session.
     */
    record Cancel(UUID userId, UUID uploadSessionId) implements Command<UploadSessionDto> {}

    /**
     * Event published when an upload session is successfully cancelled.
     */
    record Cancelled(UserId userId, UploadSessionId uploadSessionId) implements DomainEvent {}
}
