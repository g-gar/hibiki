package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.dto.CreatePlaylistCommand;
import com.ggar.hibiki.features.library.dto.PlaylistDto;
import com.ggar.hibiki.features.library.event.PlaylistCreatedEvent;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.UserId;
import com.ggar.hibiki.features.library.port.PlaylistRepository;
import com.ggar.hibiki.features.library.service.mapper.LibraryServiceMapper;
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
    private final LibraryServiceMapper libraryServiceMapper;
    private final EventBus eventBus;

    @Override
    public Mono<PlaylistDto> handle(CreatePlaylistCommand command) {
        User user = User.builder().id(UserId.of(command.getUserId())).build();

        Playlist playlist = Playlist.builder()
                .user(user)
                .name(command.getName())
                .description(command.getDescription())
                .visibility(command.getVisibility())
                .owner(true)
                .addedAt(Instant.now())
                .updatedAt(Instant.now())
                .items(new ArrayList<>())
                .build();

        return playlistRepository.save(user, playlist).flatMap(saved -> eventBus.publish(PlaylistCreatedEvent.builder()
                        .userId(user.getId())
                        .playlistId(saved.getId().getValue())
                        .name(saved.getName())
                        .build())
                .thenReturn(libraryServiceMapper.toDto(saved)));
    }
}
