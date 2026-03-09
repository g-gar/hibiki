package com.ggar.hibiki.features.ingestion.port;

import reactor.core.publisher.Mono;

/**
 * Port for object storage operations supporting multipart uploads.
 */
public interface MediaStorage {
    Mono<String> initiateMultipartUpload(String key);

    Mono<String> uploadPart(String key, String uploadId, int partNumber, byte[] content);

    Mono<Void> completeMultipartUpload(String key, String uploadId, java.util.List<String> partETags);

    Mono<Void> abortMultipartUpload(String key, String uploadId);

    Mono<Void> delete(String key);
}
