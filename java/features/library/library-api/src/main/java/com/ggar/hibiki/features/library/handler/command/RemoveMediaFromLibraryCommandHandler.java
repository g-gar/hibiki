package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.model.UserId;
import java.util.UUID;

public interface RemoveMediaFromLibraryCommandHandler
        extends CommandHandler<RemoveMediaFromLibraryCommandHandler.Remove, UUID> {

    record Remove(UUID userId, LibraryItemType type, UUID mediaId) implements Command<UUID> {}

    record MediaRemoved(UserId userId, UUID mediaId) implements DomainEvent {}
}
