package com.ggar.hibiki.features.library.port;

import com.ggar.hibiki.features.library.model.LibraryFilter;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.Pagination;
import com.ggar.hibiki.features.library.model.User;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Port interface for library persistence.
 * Defines the operations required to manage saved media items (Songs/Albums) in the library.
 */
public interface LibraryRepository {
    /**
     * Adds a media item to the user's library.
     */
    Mono<LibraryItem> save(User user, LibraryItem item);
    /**
     * Removes a media item from the user's library.
     */
    Mono<Void> remove(User user, UUID mediaId);
    /**
     * Finds a library item for a user and media ID.
     * mediaId can be the resource ID (Song/Album) or the LibraryItem ID itself.
     */
    Mono<LibraryItem> findById(User user, UUID mediaId);

    /**
     * Checks if an item exists in the user's library.
     */
    Mono<Boolean> exists(User user, UUID mediaId);
    /**
     * Finds library items based on filters and pagination.
     */
    Flux<LibraryItem> findAll(User user, LibraryFilter filter, Pagination pagination);
    /**
     * Counts the total number of items matching the filter.
     */
    Mono<Long> count(User user, LibraryFilter filter);
}
