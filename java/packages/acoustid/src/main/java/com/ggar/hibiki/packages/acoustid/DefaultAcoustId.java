package com.ggar.hibiki.packages.acoustid;

import com.ggar.hibiki.packages.acoustid.client.AcoustIdWebClient;
import com.ggar.hibiki.packages.acoustid.fpcalc.FingerprintCalculator;
import com.ggar.hibiki.packages.acoustid.model.AcoustIdLookupResponse;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Default implementation of {@link AcoustId}.
 * Orchestrates the fingerprint calculation and the API lookup.
 */
@RequiredArgsConstructor
public class DefaultAcoustId implements AcoustId {

    private final FingerprintCalculator fingerprintCalculator;
    private final AcoustIdWebClient acoustIdWebClient;

    @Override
    public Mono<AcoustIdLookupResponse> lookupByStream(InputStream audioStream) {
        return fingerprintCalculator
                .calculate(audioStream)
                .flatMap(audioFingerprint -> acoustIdWebClient.lookupByFingerprint(
                        audioFingerprint.getFingerprint(), audioFingerprint.getDurationSeconds()));
    }
}
