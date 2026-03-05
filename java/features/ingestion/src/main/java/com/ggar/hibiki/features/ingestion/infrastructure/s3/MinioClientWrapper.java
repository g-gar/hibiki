package com.ggar.hibiki.features.ingestion.infrastructure.s3;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;

/**
 * Infrastructure component interacting with the MinIO media storage.
 */
@Service
public class MinioClientWrapper {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioClientWrapper(
            @Value("${cloud.minio.url}") String minioUrl,
            @Value("${cloud.minio.access-key}") String accessKey,
            @Value("${cloud.minio.secret-key}") String secretKey,
            @Value("${cloud.minio.bucket-name:hibiki-media}") String bucketName) {

        this.minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;
    }

    /**
     * Uploads a stream as an object to MinIO reactively.
     *
     * @param objectKey   The key (e.g. UUIDv7 string) under which the object will
     *                    be stored
     * @param stream      The content stream
     * @param size        The expected size (-1 if unknown, though PartSize must
     *                    then be configured)
     * @param contentType The MIME type (e.g., audio/mpeg)
     * @return Mono completing when upload is successful
     */
    public Mono<Void> uploadStream(String objectKey, InputStream stream, long size, String contentType) {
        return Mono.fromCallable(() -> {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .stream(stream, size, -1)
                            .contentType(contentType)
                            .build());
            return null;
        }).then();
    }
}
