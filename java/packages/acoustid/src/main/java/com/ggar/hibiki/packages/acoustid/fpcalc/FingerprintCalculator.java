package com.ggar.hibiki.packages.acoustid.fpcalc;

import com.ggar.hibiki.packages.acoustid.model.AudioFingerprint;
import java.io.InputStream;
import reactor.core.publisher.Mono;

/**
 * Defines the contract for calculating audio fingerprints from an input stream.
 */
public interface FingerprintCalculator {

    /**
     * Calculates the audio fingerprint for a given audio stream.
     *
     * @param audioStream the audio stream
     * @return a {@link Mono} emitting the calculated {@link AudioFingerprint}
     */
    Mono<AudioFingerprint> calculate(InputStream audioStream);
}
