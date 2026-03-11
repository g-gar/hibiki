package com.ggar.hibiki.features.library.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Command to delete an entire playlist.
 */
@Value
@Builder(toBuilder = true)
@With
public class DeletePlaylistCommand implements Command<UUID> {
    /**
     * User identifier.
     */
    UUID userId;
    /**
     * Identifier of the playlist to delete.
     */
    UUID playlistId;
}
