package com.ggar.hibiki.features.library.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.PlaylistItem;
import com.ggar.hibiki.features.library.model.PlaylistOperation;
import com.ggar.hibiki.features.library.model.Song;
import com.ggar.hibiki.features.library.model.SongId;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.UserId;
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
    public Mono<Playlist> handle(UpdatePlaylistCommandHandler.Update command) {
        User user = User.builder().id(UserId.of(command.userId())).build();
        return playlistRepository
                .findById(user, command.playlistId())
                .flatMap(playlist -> {
                    Playlist updatedPlaylist = applyOperations(playlist, command.operations()).toBuilder()
                            .updatedAt(Instant.now())
                            .build();
                    return playlistRepository.save(user, updatedPlaylist);
                })
                .flatMap(saved -> eventBus.publish(new UpdatePlaylistCommandHandler.PlaylistUpdated(
                                user.getId(), saved.getId().getValue()))
                        .thenReturn(saved));
    }

    private Playlist applyOperations(Playlist playlist, List<PlaylistOperation> operations) {
        // Create a mutable copy of the items list
        List<PlaylistItem> items = new ArrayList<>(playlist.getItems());
        String name = playlist.getName();
        String description = playlist.getDescription();
        com.ggar.hibiki.features.library.model.Visibility visibility = playlist.getVisibility();

        for (PlaylistOperation op : operations) {
            switch (op.getType()) {
                case ADD_SONGS -> {
                    if (op.getSongIds() != null) {
                        for (UUID songId : op.getSongIds()) {
                            items.add(PlaylistItem.builder()
                                    .id(com.ggar.hibiki.features.library.model.PlaylistItemId.of(UUID.randomUUID()))
                                    .song(Song.builder().id(SongId.of(songId)).build())
                                    .addedAt(Instant.now())
                                    .position(items.size())
                                    .build());
                        }
                    }
                }
                case REMOVE_SONGS -> {
                    if (op.getSongIds() != null) {
                        items.removeIf(item ->
                                op.getSongIds().contains(item.getSong().getId().getValue()));
                    }
                }
                case MOVE_SONG -> {
                    PlaylistItem itemToMove = items.stream()
                            .filter(i -> i.getSong().getId().getValue().equals(op.getSongId()))
                            .findFirst()
                            .orElse(null);
                    if (itemToMove != null) {
                        items.remove(itemToMove);
                        items.add(Math.min(op.getNewPosition(), items.size()), itemToMove);
                    }
                }
                case UPDATE_METADATA -> {
                    if (op.getNewName() != null) name = op.getNewName();
                    if (op.getNewDescription() != null) description = op.getNewDescription();
                }
                case UPDATE_VISIBILITY -> {
                    if (op.getVisibility() != null) visibility = op.getVisibility();
                }
                default -> throw new IllegalArgumentException("Unknown operation type: " + op.getType());
            }
        }

        // Re-calculate positions to ensure consistency
        for (int i = 0; i < items.size(); i++) {
            items.set(i, items.get(i).toBuilder().position(i).build());
        }

        return playlist.toBuilder()
                .items(items)
                .name(name)
                .description(description)
                .visibility(visibility)
                .build();
    }
}
