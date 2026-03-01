package com.ggar.hibiki.packages.acoustid.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the result of an audio track fingerprint calculation.
 * Contains both the generated fingerprint string and the audio track duration.
 */
@Data
@Builder
public class AudioFingerprint {
    private String fingerprint;
    private int durationSeconds;
}
