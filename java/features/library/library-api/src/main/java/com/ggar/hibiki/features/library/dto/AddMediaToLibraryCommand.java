package com.ggar.hibiki.features.library.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Command to add a media item (Song or Album) to the user's library.
 */
@Value
@Builder(toBuilder = true)
@With
public class AddMediaToLibraryCommand implements Command<LibraryItemDto> {
    /**
     * User identifier.
     */
    UUID userId;
    /**
     * Type of media to add (SONG or ALBUM).
     */
    LibraryItemType type;
    /**
     * Identifier of the media in the catalog.
     */
    UUID mediaId;
}
