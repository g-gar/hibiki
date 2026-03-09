package com.ggar.hibiki.features.ingestion.infrastructure.adapter;

import com.ggar.hibiki.features.ingestion.port.MediaTypeResolver;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Tika-based MediaTypeResolver. Detects MIME type from magic bytes and filename.
 */
@Component
public class TikaMediaTypeResolver implements MediaTypeResolver {

    private final Tika tika = new Tika();

    @Override
    public Mono<String> resolve(byte[] header, String filename) {
        return Mono.fromCallable(() -> {
                    try {
                        return tika.detect(header, filename);
                    } catch (Exception e) {
                        return "application/octet-stream";
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
