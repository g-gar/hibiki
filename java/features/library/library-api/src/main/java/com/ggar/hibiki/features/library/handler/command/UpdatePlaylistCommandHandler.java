package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.PlaylistOperation;
import com.ggar.hibiki.features.library.model.UserId;
import java.util.List;
import java.util.UUID;

public interface UpdatePlaylistCommandHandler extends CommandHandler<UpdatePlaylistCommandHandler.Update, Playlist> {

    record Update(UUID userId, UUID playlistId, List<PlaylistOperation> operations) implements Command<Playlist> {}

    record PlaylistUpdated(UserId userId, UUID playlistId) implements DomainEvent {}
}
