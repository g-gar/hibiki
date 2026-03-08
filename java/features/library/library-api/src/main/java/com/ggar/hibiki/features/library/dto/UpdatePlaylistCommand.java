package com.ggar.hibiki.features.library.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.library.model.IdentityContext;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.PlaylistOperation;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Command to perform batch operations on a playlist.
 */
@Value
@Builder(toBuilder = true)
@With
public class UpdatePlaylistCommand implements Command<Playlist> {
    /**
     * User identity context.
     */
    IdentityContext identityContext;
    /**
     * Identifier of the playlist to update.
     */
    UUID playlistId;
    /**
     * List of operations to perform (add, remove, move, update metadata).
     */
    List<PlaylistOperation> operations;
}
