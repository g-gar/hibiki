package com.ggar.hibiki.features.library.model;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Filter strategy for library queries.
 */
@Value
@Builder(toBuilder = true)
@With
public class LibraryFilter {
    /**
     * Filter by specific media types.
     */
    List<LibraryItemType> types;
    /**
     * Filter by a specific playlist ID.
     */
    UUID playlistId;
    /**
     * Search term for titles or descriptions.
     */
    String searchTerm;
    /**
     * Filter by a specific artist.
     */
    Artist artist;
    /**
     * Filter by a specific album.
     */
    Album album;
    /**
     * Filter by specific item IDs.
     */
    List<UUID> ids;
    /**
     * Filter by visibility.
     */
    Visibility visibility;
    /**
     * Filter by ownership.
     */
    Boolean isOwner;
}
