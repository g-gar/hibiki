package com.ggar.hibiki.features.metadata.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.metadata.model.FingerprintResult;
import com.ggar.hibiki.features.metadata.model.MediaId;
import lombok.Builder;
import lombok.Value;

/**
 * Command to extract an AcoustID fingerprint from an audio file.
 */
@Value
@Builder
public class ExtractFingerprintCommand implements Command<FingerprintResult> {
    MediaId mediaId;
    String mimeType;
}
