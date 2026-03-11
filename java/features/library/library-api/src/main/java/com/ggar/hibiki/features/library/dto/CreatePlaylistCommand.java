package com.ggar.hibiki.features.library.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.Visibility;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Command to create a new playlist.
 */
@Value
@Builder(toBuilder = true)
@With
public class CreatePlaylistCommand implements Command<Playlist> {
    /**
     * User identifier.
     */
    UUID userId;
    /**
     * Name of the new playlist.
     */
    String name;
    /**
     * Optional description.
     */
    String description;
    /**
     * Visibility of the new playlist.
     */
    Visibility visibility;
}
