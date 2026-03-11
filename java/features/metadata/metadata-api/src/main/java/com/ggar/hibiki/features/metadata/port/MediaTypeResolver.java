package com.ggar.hibiki.features.metadata.port;

import reactor.core.publisher.Mono;

/**
 * Port for MIME type detection from file headers (magic bytes).
 * Used inline by the chunk handler on the first chunk, not as a pipeline stage.
 */
public interface MediaTypeResolver {
    Mono<String> resolve(byte[] header, String filename);
}
