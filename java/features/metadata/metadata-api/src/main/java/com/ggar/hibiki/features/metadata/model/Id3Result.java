package com.ggar.hibiki.features.metadata.model;

import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import lombok.Builder;
import lombok.Value;

/**
 * Result of an ID3v2 tag mapping operation.
 */
@Value
@Builder
public class Id3Result {
    MediaId mediaId;
    Id3v2Tag tags;
}
