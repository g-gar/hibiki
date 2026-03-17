package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.UserId;
import com.ggar.hibiki.features.library.port.PlaylistRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of DeletePlaylistCommandHandler.
 */
@Service
@RequiredArgsConstructor
public class DeletePlaylistCommandHandlerImpl implements DeletePlaylistCommandHandler {

    private final PlaylistRepository playlistRepository;
    private final EventBus eventBus;

    @Override
    public Mono<UUID> handle(DeletePlaylistCommandHandler.Delete command) {
        User user = User.builder().id(UserId.of(command.userId())).build();
        UUID playlistId = command.playlistId();

        return playlistRepository
                .delete(user, playlistId)
                .then(eventBus.publish(new DeletePlaylistCommandHandler.PlaylistDeleted(user.getId(), playlistId)))
                .thenReturn(playlistId);
    }
}
