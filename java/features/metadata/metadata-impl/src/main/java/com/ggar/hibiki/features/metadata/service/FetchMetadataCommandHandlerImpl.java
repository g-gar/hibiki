package com.ggar.hibiki.features.metadata.service;

import com.ggar.hibiki.features.metadata.dto.FetchMetadataCommand;
import com.ggar.hibiki.features.metadata.model.FetchMetadataResult;
import com.ggar.hibiki.features.metadata.model.MediaId;
import com.ggar.hibiki.packages.acoustid.client.AcoustIdWebClient;
import com.ggar.hibiki.packages.musicbrainz.MusicBrainz;
import com.ggar.hibiki.packages.musicbrainz.model.Include;
import com.ggar.hibiki.packages.musicbrainz.model.IsrcResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchMetadataCommandHandlerImpl implements FetchMetadataCommandHandler {

    private final AcoustIdWebClient acoustIdWebClient;
    private final MusicBrainz musicBrainz;

    @Override
    public Publisher<FetchMetadataResult> handle(FetchMetadataCommand command) {
        log.info("Fetching metadata for mediaId: {}, fingerprintId: {}", command.getMediaId(), command.getAcoustId());

        // 1. Query AcoustID to resolve the fingerprint into an ISRC.
        // The API requires a duration; we'll provide an estimated 120s if not given.
        return acoustIdWebClient
                .lookupByFingerprint(command.getAcoustId(), 120)
                .flatMap(acoustIdResponse -> {
                    if (!"ok".equals(acoustIdResponse.getStatus())
                            || acoustIdResponse.getResults() == null
                            || acoustIdResponse.getResults().isEmpty()) {
                        log.warn("No match found in AcoustID for mediaId: {}", command.getMediaId());
                        return Mono.empty();
                    }

                    // Extract the first available ISRC from the recordings
                    return Flux.fromIterable(
                                    acoustIdResponse.getResults().get(0).getRecordings())
                            .filter(rec ->
                                    rec.getIsrcs() != null && !rec.getIsrcs().isEmpty())
                            .map(rec -> rec.getIsrcs().get(0))
                            .next(); // Get the first ISRC we find
                })
                .flatMap(isrc -> {
                    log.info("Found ISRC {} for mediaId {}, querying MusicBrainz", isrc, command.getMediaId());

                    // 2. Query MusicBrainz with the ISRC
                    long includeMask = Include.RECORDINGS.getMask()
                            | Include.RELEASES.getMask()
                            | Include.ARTIST_CREDITS.getMask();

                    return musicBrainz.lookupByIsrc(isrc, IsrcResponse.class, includeMask);
                })
                .map(isrcResponse -> FetchMetadataResult.builder()
                        .mediaId(MediaId.of(command.getMediaId()))
                        .metadata(isrcResponse)
                        .build())
                .doOnError(e -> log.error("Error fetching metadata for mediaId: {}", command.getMediaId(), e));
    }
}
