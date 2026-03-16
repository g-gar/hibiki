package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.UserId;
import com.ggar.hibiki.features.library.port.PlaylistRepository;
import java.time.Instant;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of CreatePlaylistCommandHandler.
 */
@Service
@RequiredArgsConstructor
public class CreatePlaylistCommandHandlerImpl implements CreatePlaylistCommandHandler {

    private final PlaylistRepository playlistRepository;
    private final EventBus eventBus;

    @Override
    public Mono<Playlist> handle(CreatePlaylistCommandHandler.Create command) {
        User user = User.builder().id(UserId.of(command.userId())).build();

        Playlist playlist = Playlist.builder()
                .user(user)
                .name(command.name())
                .description(command.description())
                .visibility(command.visibility())
                .owner(true)
                .addedAt(Instant.now())
                .updatedAt(Instant.now())
                .items(new ArrayList<>())
                .build();

        return playlistRepository.save(user, playlist).flatMap(saved -> eventBus.publish(
                        new CreatePlaylistCommandHandler.PlaylistCreated(
                                user.getId(), saved.getId().getValue(), saved.getName()))
                .thenReturn(saved));
    }
}
