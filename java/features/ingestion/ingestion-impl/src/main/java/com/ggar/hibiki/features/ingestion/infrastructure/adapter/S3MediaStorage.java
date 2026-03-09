package com.ggar.hibiki.features.ingestion.infrastructure.adapter;

import com.ggar.hibiki.features.ingestion.port.MediaStorage;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;

/**
 * Vendor-agnostic S3 adapter implementing the MediaStorage port via the AWS S3 SDK v2.
 * Works with any S3-compatible server (SeaweedFS, MinIO, Garage, Ceph, etc.)
 * — only the endpoint and credentials change.
 */
@Slf4j
@Component
public class S3MediaStorage implements MediaStorage {

    private final S3Client s3Client;
    private final String bucketName;

    public S3MediaStorage(S3Client s3Client, @Value("${storage.s3.bucket-name:hibiki-media}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public Mono<String> initiateMultipartUpload(String key) {
        return Mono.fromCallable(() -> {
                    var response = s3Client.createMultipartUpload(CreateMultipartUploadRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build());
                    log.debug("Initiated multipart upload for key={}, uploadId={}", key, response.uploadId());
                    return response.uploadId();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<String> uploadPart(String key, String uploadId, int partNumber, byte[] content) {
        return Mono.fromCallable(() -> {
                    var response = s3Client.uploadPart(
                            UploadPartRequest.builder()
                                    .bucket(bucketName)
                                    .key(key)
                                    .uploadId(uploadId)
                                    .partNumber(partNumber)
                                    .build(),
                            RequestBody.fromBytes(content));
                    log.debug("Uploaded part {} for key={}, etag={}", partNumber, key, response.eTag());
                    return response.eTag();
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> completeMultipartUpload(String key, String uploadId, List<String> partETags) {
        return Mono.fromCallable(() -> {
                    List<CompletedPart> completedParts = IntStream.range(0, partETags.size())
                            .mapToObj(i -> CompletedPart.builder()
                                    .partNumber(i + 1)
                                    .eTag(partETags.get(i))
                                    .build())
                            .toList();

                    s3Client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .uploadId(uploadId)
                            .multipartUpload(CompletedMultipartUpload.builder()
                                    .parts(completedParts)
                                    .build())
                            .build());

                    log.info("Completed multipart upload for key={}", key);
                    return (Void) null;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> abortMultipartUpload(String key, String uploadId) {
        return Mono.fromCallable(() -> {
                    s3Client.abortMultipartUpload(AbortMultipartUploadRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .uploadId(uploadId)
                            .build());
                    log.info("Aborted multipart upload for key={}, uploadId={}", key, uploadId);
                    return (Void) null;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> delete(String key) {
        return Mono.fromCallable(() -> {
                    s3Client.deleteObject(DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build());
                    log.info("Deleted object key={}", key);
                    return (Void) null;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
