package com.ggar.hibiki.features.metadata.handler.command;

import com.ggar.hibiki.features.metadata.model.FetchMetadataResult;
import com.ggar.hibiki.features.metadata.model.MediaId;
import com.ggar.hibiki.packages.acoustid.client.AcoustIdWebClient;
import com.ggar.hibiki.packages.musicbrainz.MusicBrainz;
import com.ggar.hibiki.packages.musicbrainz.model.Include;
import com.ggar.hibiki.packages.musicbrainz.model.IsrcResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchMetadataCommandHandlerImpl implements FetchMetadataCommandHandler {

    private final AcoustIdWebClient acoustIdWebClient;
    private final MusicBrainz musicBrainz;
    private final com.ggar.hibiki.core.shared.event.EventBus eventBus;

    @Override
    public reactor.core.publisher.Mono<FetchMetadataResult> handle(FetchMetadataCommandHandler.Fetch command) {
        log.info("Fetching metadata for mediaId: {}, fingerprintId: {}", command.mediaId(), command.acoustId());

        return acoustIdWebClient
                .lookupByFingerprint(command.acoustId(), 120)
                .flatMap(acoustIdResponse -> {
                    if (!"ok".equals(acoustIdResponse.getStatus())
                            || acoustIdResponse.getResults() == null
                            || acoustIdResponse.getResults().isEmpty()) {
                        log.warn("No match found in AcoustID for mediaId: {}", command.mediaId());
                        return Mono.empty();
                    }

                    return Flux.fromIterable(
                                    acoustIdResponse.getResults().get(0).getRecordings())
                            .filter(rec ->
                                    rec.getIsrcs() != null && !rec.getIsrcs().isEmpty())
                            .map(rec -> rec.getIsrcs().get(0))
                            .next();
                })
                .flatMap(isrc -> {
                    log.info("Found ISRC {} for mediaId {}, querying MusicBrainz", isrc, command.mediaId());

                    long includeMask = Include.RECORDINGS.getMask()
                            | Include.RELEASES.getMask()
                            | Include.ARTIST_CREDITS.getMask();

                    return musicBrainz.lookupByIsrc(isrc, IsrcResponse.class, includeMask);
                })
                .flatMap(isrcResponse -> {
                    FetchMetadataResult result = FetchMetadataResult.builder()
                            .mediaId(MediaId.of(command.mediaId()))
                            .metadata(isrcResponse)
                            .build();

                    return eventBus.publish(new FetchMetadataCommandHandler.Fetched(command.mediaId(), isrcResponse))
                            .thenReturn(result);
                })
                .doOnError(e -> log.error("Error fetching metadata for mediaId: {}", command.mediaId(), e));
    }
}
