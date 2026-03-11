package com.ggar.hibiki.features.metadata.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.metadata.model.FetchMetadataResult;
import com.ggar.hibiki.features.metadata.model.FingerprintId;
import com.ggar.hibiki.features.metadata.model.MediaId;
import lombok.Builder;
import lombok.Value;

/**
 * Command to lookup metadata (e.g. from MusicBrainz) using a fingerprint.
 */
@Value
@Builder
public class FetchMetadataCommand implements Command<FetchMetadataResult> {
    MediaId mediaId;
    FingerprintId acoustId;
    String mimeType;
}
