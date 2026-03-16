package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.model.UserId;
import java.util.UUID;

public interface AddMediaToLibraryCommandHandler
        extends CommandHandler<AddMediaToLibraryCommandHandler.Add, LibraryItem> {

    record Add(UUID userId, LibraryItemType type, UUID mediaId) implements Command<LibraryItem> {}

    record MediaAdded(UserId userId, UUID mediaId, LibraryItemType type) implements DomainEvent {}
}
