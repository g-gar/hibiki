package com.ggar.hibiki.features.metadata.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.metadata.model.FetchMetadataResult;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Command to lookup metadata (e.g. from MusicBrainz) using a fingerprint.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class FetchMetadataCommand implements Command<FetchMetadataResult> {
    UUID mediaId;
    String acoustId;
    String mimeType;
}
