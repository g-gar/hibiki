package com.ggar.hibiki.features.library.port;

import com.ggar.hibiki.features.library.model.LibraryFilter;
import com.ggar.hibiki.features.library.model.Pagination;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.User;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Port interface for playlist persistence.
 * Defines the operations required to manage user-defined collections.
 */
public interface PlaylistRepository {
    /**
     * Saves or updates a playlist.
     */
    Mono<Playlist> save(User user, Playlist playlist);
    /**
     * Finds a playlist by ID, ensuring the user has access according to entitlement/access rules.
     */
    Mono<Playlist> findById(User user, UUID playlistId);
    /**
     * Deletes a playlist.
     */
    Mono<Void> delete(User user, UUID playlistId);
    /**
     * Finds playlists belonging to a user based on filters and pagination.
     */
    Flux<Playlist> findAll(User user, LibraryFilter filter, Pagination pagination);
    /**
     * Counts the total number of playlists for a user matching the filter.
     */
    Mono<Long> count(User user, LibraryFilter filter);
}
