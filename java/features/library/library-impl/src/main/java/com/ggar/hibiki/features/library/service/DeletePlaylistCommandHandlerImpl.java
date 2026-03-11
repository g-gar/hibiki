package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.dto.DeletePlaylistCommand;
import com.ggar.hibiki.features.library.event.PlaylistDeletedEvent;
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
    public Mono<UUID> handle(DeletePlaylistCommand command) {
        User user = User.builder().id(UserId.of(command.getUserId())).build();
        UUID playlistId = command.getPlaylistId();

        return playlistRepository
                .delete(user, playlistId)
                .then(eventBus.publish(PlaylistDeletedEvent.builder()
                        .userId(user.getId())
                        .playlistId(playlistId)
                        .build()))
                .thenReturn(playlistId);
    }
}
