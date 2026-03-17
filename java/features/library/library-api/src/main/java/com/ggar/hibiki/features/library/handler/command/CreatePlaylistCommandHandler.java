package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.UserId;
import com.ggar.hibiki.features.library.model.Visibility;
import java.util.UUID;

public interface CreatePlaylistCommandHandler extends CommandHandler<CreatePlaylistCommandHandler.Create, Playlist> {

    record Create(UUID userId, String name, String description, Visibility visibility) implements Command<Playlist> {}

    record PlaylistCreated(UserId userId, UUID playlistId, String name) implements DomainEvent {}
}
