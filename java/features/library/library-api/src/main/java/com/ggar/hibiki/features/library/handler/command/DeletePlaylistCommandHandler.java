package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.model.UserId;
import java.util.UUID;

public interface DeletePlaylistCommandHandler extends CommandHandler<DeletePlaylistCommandHandler.Delete, UUID> {

    record Delete(UUID userId, UUID playlistId) implements Command<UUID> {}

    record PlaylistDeleted(UserId userId, UUID playlistId) implements DomainEvent {}
}
