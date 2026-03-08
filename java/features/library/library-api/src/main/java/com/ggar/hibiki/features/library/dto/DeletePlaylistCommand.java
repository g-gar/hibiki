package com.ggar.hibiki.features.library.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.library.model.IdentityContext;
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
     * User identity context.
     */
    IdentityContext identityContext;
    /**
     * Identifier of the playlist to delete.
     */
    UUID playlistId;
}
