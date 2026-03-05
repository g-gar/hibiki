package com.ggar.hibiki.packages.acoustid;

import com.ggar.hibiki.packages.acoustid.model.AcoustIdLookupResponse;
import java.io.InputStream;
import reactor.core.publisher.Mono;

/**
 * Main entry point for the AcoustID module.
 * Allows retrieving recording identifiers starting from an audio stream.
 */
public interface AcoustId {

    /**
     * Executes the fingerprint calculation and looks up the corresponding
     * recordings and ISRCs in the AcoustID API.
     *
     * @param audioStream the input stream containing the audio data
     * @return a {@link Mono} returning the AcoustID payload
     */
    Mono<AcoustIdLookupResponse> lookupByStream(InputStream audioStream);
}
