package com.ggar.hibiki.features.metadata.model;

import lombok.Builder;
import lombok.Value;

/**
 * Result of a fingerprint extraction operation.
 */
@Value
@Builder
public class FingerprintResult {
    MediaId mediaId;
    FingerprintId acoustId;
}
