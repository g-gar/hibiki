package com.ggar.hibiki.features.library.service;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.dto.UpdatePlaylistCommand;
import com.ggar.hibiki.features.library.event.PlaylistUpdatedEvent;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.PlaylistItem;
import com.ggar.hibiki.features.library.model.PlaylistOperation;
import com.ggar.hibiki.features.library.model.Song;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.port.PlaylistRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of UpdatePlaylistCommandHandler.
 * Handles batch operations on a playlist.
 */
@Service
@RequiredArgsConstructor
public class UpdatePlaylistCommandHandlerImpl implements UpdatePlaylistCommandHandler {

    private final PlaylistRepository playlistRepository;
    private final EventBus eventBus;

    @Override
    public Mono<Playlist> handle(UpdatePlaylistCommand command) {
        User user = command.getIdentityContext().getUser();
        return playlistRepository
                .findById(user, command.getPlaylistId())
                .flatMap(playlist -> {
                    Playlist updatedPlaylist = applyOperations(playlist, command.getOperations());
                    updatedPlaylist.setUpdatedAt(Instant.now());
                    return playlistRepository.save(user, updatedPlaylist);
                })
                .flatMap(saved -> eventBus.publish(new PlaylistUpdatedEvent(user.getId(), saved.getId()))
                        .thenReturn(saved));
    }

    private Playlist applyOperations(Playlist playlist, List<PlaylistOperation> operations) {
        // Create a mutable copy of the items list
        List<PlaylistItem> items = new ArrayList<>(playlist.getItems());

        for (PlaylistOperation op : operations) {
            switch (op.getType()) {
                case ADD_SONGS -> {
                    if (op.getSongIds() != null) {
                        for (UUID songId : op.getSongIds()) {
                            items.add(PlaylistItem.builder()
                                    .id(UUID.randomUUID())
                                    .song(Song.builder().id(songId).build())
                                    .addedAt(Instant.now())
                                    .position(items.size())
                                    .build());
                        }
                    }
                }
                case REMOVE_SONGS -> {
                    if (op.getSongIds() != null) {
                        items.removeIf(
                                item -> op.getSongIds().contains(item.getSong().getId()));
                    }
                }
                case MOVE_SONG -> {
                    PlaylistItem itemToMove = items.stream()
                            .filter(i -> i.getSong().getId().equals(op.getSongId()))
                            .findFirst()
                            .orElse(null);
                    if (itemToMove != null) {
                        items.remove(itemToMove);
                        items.add(Math.min(op.getNewPosition(), items.size()), itemToMove);
                    }
                }
                case UPDATE_METADATA -> {
                    if (op.getNewName() != null) playlist.setName(op.getNewName());
                    if (op.getNewDescription() != null) playlist.setDescription(op.getNewDescription());
                }
                case UPDATE_VISIBILITY -> {
                    if (op.getVisibility() != null) playlist.setVisibility(op.getVisibility());
                }
                default -> throw new IllegalArgumentException("Unknown operation type: " + op.getType());
            }
        }

        // Re-calculate positions to ensure consistency
        for (int i = 0; i < items.size(); i++) {
            items.set(i, items.get(i).toBuilder().position(i).build());
        }

        return playlist.toBuilder().items(items).build();
    }
}
