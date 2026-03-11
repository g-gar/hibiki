package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.catalog.dto.CatalogCreationResult;
import com.ggar.hibiki.core.catalog.dto.CreateCatalogItemsCommand;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.ingestion.dto.CompleteUploadCommand;
import com.ggar.hibiki.features.ingestion.dto.GetUploadSessionByIdQuery;
import com.ggar.hibiki.features.ingestion.dto.InitiateUploadCommand;
import com.ggar.hibiki.features.ingestion.dto.ItemDescriptor;
import com.ggar.hibiki.features.ingestion.dto.UploadChunkCommand;
import com.ggar.hibiki.features.ingestion.model.IdentityContext;
import com.ggar.hibiki.features.ingestion.model.UploadItem;
import com.ggar.hibiki.features.ingestion.model.UploadProgress;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.library.dto.AddMediaToLibraryCommand;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.metadata.dto.FetchMetadataCommand;
import com.ggar.hibiki.features.metadata.dto.MapToId3Command;
import com.ggar.hibiki.features.metadata.model.FetchMetadataResult;
import com.ggar.hibiki.features.metadata.model.FingerprintId;
import com.ggar.hibiki.features.metadata.model.Id3Result;
import com.ggar.hibiki.features.metadata.model.MediaId;
import com.ggar.hibiki.features.metadata.port.ContentHasher;
import com.ggar.hibiki.features.metadata.port.MediaTypeResolver;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Slf4j
@Service
public class IngestMediaUseCase extends BaseOrchestratorUseCase {

    private final MediaTypeResolver mediaTypeResolver;
    private final ContentHasher contentHasher;

    public IngestMediaUseCase(Mediator mediator, MediaTypeResolver mediaTypeResolver, ContentHasher contentHasher) {
        super(mediator);
        this.mediaTypeResolver = mediaTypeResolver;
        this.contentHasher = contentHasher;
    }

    /**
     * Orchestrates the audio ingestion process by translating an incoming file stream
     * into a sequence of Initiate, Chunk, and Complete commands for the Ingestion module.
     * Starts a session, streams the chunks to storage, and completes it.
     *
     * @param identityContext User identity context.
     * @param filename        Name of the file being uploaded.
     * @param expectedSize    Expected size of the upload stream.
     * @param content         Stream of file byte chunks.
     * @return Mono completing with the Media ID (which is the Upload Session ID).
     */
    public Mono<String> execute(
            IdentityContext identityContext, String filename, long expectedSize, Flux<DataBuffer> content) {

        ItemDescriptor itemDescriptor = ItemDescriptor.builder()
                .originalFilename(filename)
                .expectedSize(expectedSize)
                .build();

        InitiateUploadCommand initiateCmd = InitiateUploadCommand.builder()
                .identityContext(identityContext)
                .items(List.of(itemDescriptor))
                .build();

        return Mono.from(mediator.send(initiateCmd))
                .flatMap(session -> {
                    UploadItem item = session.getItems().get(0);

                    AtomicReference<String> resolvedMimeType = new AtomicReference<>(null);
                    AtomicReference<ContentHasher.HashState> hashState = new AtomicReference<>(contentHasher.init());
                    AtomicInteger chunkCount = new AtomicInteger(0);

                    Runnable onStart =
                            () -> log.info("Upload stream started in Orchestrator for session {}", session.getId());
                    Runnable onComplete =
                            () -> log.info("Upload stream completed in Orchestrator for session {}", session.getId());
                    Consumer<byte[]> onChunk = bytes -> {
                        if (chunkCount.getAndIncrement() == 0) {
                            mediaTypeResolver
                                    .resolve(bytes, item.getOriginalFilename())
                                    .subscribe(resolvedMimeType::set);
                        }
                        hashState.set(contentHasher.update(hashState.get(), bytes));
                        log.debug("Orchestrator processed chunk of {} bytes in-flight", bytes.length);
                    };

                    UploadChunkCommand chunkCmd = UploadChunkCommand.builder()
                            .identityContext(identityContext)
                            .uploadSessionId(session.getId())
                            .itemId(item.getId())
                            .chunkIndex(0) // Logical single chunk mapping the whole flux
                            .content(content)
                            .onUploadStarted(onStart)
                            .onChunkProcessed(onChunk)
                            .onUploadCompleted(onComplete)
                            .build();

                    return Mono.from(mediator.send(chunkCmd)).map(r -> {
                        UploadProgress progress = (UploadProgress) r;
                        String mimeTypeStr =
                                resolvedMimeType.get() != null ? resolvedMimeType.get() : "application/octet-stream";
                        return Tuples.of(
                                progress.getUploadSessionId(), mimeTypeStr, contentHasher.finalize(hashState.get()));
                    });
                })
                .flatMap(tuple -> {
                    UploadSessionId sessionId = tuple.getT1();
                    String mimeType = tuple.getT2();
                    String finalHash = tuple.getT3();

                    CompleteUploadCommand completeCmd = CompleteUploadCommand.builder()
                            .identityContext(identityContext)
                            .uploadSessionId(sessionId)
                            .mimeType(mimeType)
                            .contentHash(finalHash)
                            .build();

                    return Mono.from(mediator.send(completeCmd))
                            .thenReturn(sessionId.getId().toString());
                });
    }

    /**
     * Resumes the ingestion pipeline after the S3 upload and Lambda fingerprinting.
     * Sequentially sends commands to Metadata, Catalog, Library, Social, and Metrics
     * using the pure Command-based choreography pattern.
     */
    public Mono<Void> processMetadata(MediaId mediaId, FingerprintId fingerprintId) {
        log.info(
                "Orchestrator continuing strict ingestion pipeline for mediaId: {}, fingerprintId: {}",
                mediaId,
                fingerprintId);

        FetchMetadataCommand fetchCmd = FetchMetadataCommand.builder()
                .mediaId(mediaId)
                .acoustId(fingerprintId)
                .mimeType("audio/mpeg") // Safe fallback, assuming audio for fingerprinting
                .build();

        return Mono.from(mediator.send(fetchCmd))
                .cast(FetchMetadataResult.class)
                .flatMap(metadataResult -> {
                    MapToId3Command mapCmd = MapToId3Command.builder()
                            .mediaId(mediaId)
                            .rawMetadata(metadataResult.getMetadata())
                            .build();
                    return Mono.from(mediator.send(mapCmd)).cast(Id3Result.class);
                })
                .flatMap(id3Result -> {
                    CreateCatalogItemsCommand catalogCmd = CreateCatalogItemsCommand.builder()
                            .mediaId(mediaId.getId().toString())
                            .id3Tag(id3Result.getTags())
                            .build();
                    return Mono.from(mediator.send(catalogCmd)).cast(CatalogCreationResult.class);
                })
                .flatMap(catalogResult -> {
                    log.info(
                            "Orchestrator successfully created catalog items: Song {}, Album {}",
                            catalogResult.getSongId(),
                            catalogResult.getAlbumId());

                    // We need the User Identity context to add the item to their Library
                    GetUploadSessionByIdQuery sessionQuery = GetUploadSessionByIdQuery.builder()
                            .uploadSessionId(new UploadSessionId(mediaId.getId()))
                            .build();

                    return Mono.from(mediator.send(sessionQuery)).flatMap(uploadSession -> {
                        com.ggar.hibiki.features.library.model.User libraryUser =
                                new com.ggar.hibiki.features.library.model.User(
                                        uploadSession.getUserId().getId().getId());
                        com.ggar.hibiki.features.library.model.IdentityContext libraryIdentity =
                                com.ggar.hibiki.features.library.model.IdentityContext.builder()
                                        .user(libraryUser)
                                        .build();

                        AddMediaToLibraryCommand addLibraryCmd = AddMediaToLibraryCommand.builder()
                                .identityContext(libraryIdentity)
                                .mediaId(UUID.fromString(catalogResult.getSongId()))
                                .type(LibraryItemType.SONG)
                                .build();

                        return Mono.from(mediator.send(addLibraryCmd));
                    });
                })
                .flatMap(libraryItem -> {
                    log.info("Orchestrator successfully added song to user library: {}", libraryItem.getId());
                    // Placeholder for future steps:
                    // mediator.send(new NotifyFollowersCommand(...))
                    // mediator.send(new InitializeMetricsCommand(...))
                    return Mono.empty();
                });
    }
}
