package com.ggar.hibiki.features.metadata.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.metadata.model.Id3Result;
import com.ggar.hibiki.packages.musicbrainz.model.IsrcResponse;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Command to map raw metadata into standard ID3v2 tags.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class MapToId3Command implements Command<Id3Result> {
    UUID mediaId;
    IsrcResponse rawMetadata;
}
