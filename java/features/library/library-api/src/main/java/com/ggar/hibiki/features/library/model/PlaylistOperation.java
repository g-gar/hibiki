package com.ggar.hibiki.features.library.model;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Details of an operation to be performed on a playlist.
 */
@Value
@Builder(toBuilder = true)
@With
public class PlaylistOperation {
    /**
     * Type of operation.
     */
    PlaylistOperationType type;
    /**
     * IDs of the songs to add or remove.
     */
    List<UUID> songIds;
    /**
     * ID of the song to move.
     */
    UUID songId;
    /**
     * New position for the song being moved.
     */
    Integer newPosition;
    /**
     * New name for the playlist (metadata update).
     */
    String newName;
    /**
     * New description for the playlist (metadata update).
     */
    String newDescription;
    /**
     * New visibility for the playlist (visibility update).
     */
    Visibility visibility;
}
