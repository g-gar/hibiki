package com.ggar.hibiki.features.metadata.model;

import com.ggar.hibiki.packages.musicbrainz.model.IsrcResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Result of fetching metadata from an external source using an ISRC or fingerprint.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class FetchMetadataResult {
    MediaId mediaId;
    IsrcResponse metadata;
}
