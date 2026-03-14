package com.ggar.hibiki.features.library.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Command to remove a media item (Song or Album) from the user's library.
 */
@Value
@Builder(toBuilder = true)
@With
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RemoveMediaFromLibraryCommand implements Command<UUID> {
    /**
     * User identifier.
     */
    UUID userId;
    /**
     * Type of media to remove.
     */
    LibraryItemType type;
    /**
     * Identifier of the media in the catalog or library.
     */
    UUID mediaId;
}
