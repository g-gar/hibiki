package com.ggar.hibiki.features.ingestion.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.dto.ItemDescriptor;
import com.ggar.hibiki.features.ingestion.dto.UploadSessionDto;
import java.util.List;
import java.util.UUID;

/**
 * Interface for the handler responsible for initiating a new upload session.
 */
public interface InitiateUploadCommandHandler
        extends CommandHandler<InitiateUploadCommandHandler.Initiate, UploadSessionDto> {

    /**
     * Command to initiate a new upload session with one or more items.
     */
    record Initiate(UUID userId, List<ItemDescriptor> items) implements Command<UploadSessionDto> {}

    /**
     * Event published when a new upload session is successfully initiated.
     */
    record Initiated(UUID userId, UUID uploadSessionId) implements DomainEvent {}
}
