package com.ggar.hibiki.features.metadata.model;

import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Result of an ID3v2 tag mapping operation.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Id3Result {
    MediaId mediaId;
    Id3v2Tag tags;
}
