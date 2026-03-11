package com.ggar.hibiki.features.metadata.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.metadata.model.FingerprintResult;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Command to extract an AcoustID fingerprint from an audio file.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class ExtractFingerprintCommand implements Command<FingerprintResult> {
    UUID mediaId;
    String mimeType;
}
