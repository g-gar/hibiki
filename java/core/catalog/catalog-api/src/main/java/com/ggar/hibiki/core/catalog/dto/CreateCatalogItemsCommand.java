package com.ggar.hibiki.core.catalog.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import lombok.Builder;
import lombok.Value;
import java.util.UUID;

/**
 * Creates artists, albums, and songs in the catalog by parsing standard ID3 frames.
 * The catalog system is completely decoupled from any external metadata provider
 * and only understands standard ID3v2 frames (e.g. TIT2, TPE1, TALB).
 */
@Value
@Builder
public class CreateCatalogItemsCommand implements Command<CatalogCreationResult> {
    UUID mediaId;
    Id3v2Tag id3Tag;
}
