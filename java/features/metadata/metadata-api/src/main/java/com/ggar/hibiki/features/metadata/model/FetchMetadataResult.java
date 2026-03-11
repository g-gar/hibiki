package com.ggar.hibiki.features.metadata.model;

import com.ggar.hibiki.packages.musicbrainz.model.IsrcResponse;
import lombok.Builder;
import lombok.Value;

/**
 * Result of fetching metadata from an external source using an ISRC or fingerprint.
 */
@Value
@Builder
public class FetchMetadataResult {
    MediaId mediaId;
    IsrcResponse metadata;
}
