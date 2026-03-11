package com.ggar.hibiki.features.metadata.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.metadata.model.Id3Result;
import com.ggar.hibiki.features.metadata.model.MediaId;
import com.ggar.hibiki.packages.musicbrainz.model.IsrcResponse;
import lombok.Builder;
import lombok.Value;

/**
 * Command to map raw metadata into standard ID3v2 tags.
 */
@Value
@Builder
public class MapToId3Command implements Command<Id3Result> {
    MediaId mediaId;
    IsrcResponse rawMetadata;
}
