package com.ggar.hibiki.packages.musicbrainz;

import com.ggar.hibiki.packages.musicbrainz.client.MusicBrainzWebClient;
import com.ggar.hibiki.packages.musicbrainz.model.EntityType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * Default implementation of the {@link MusicBrainz} API.
 * Uses a reactive {@link MusicBrainzWebClient} and leverages Spring's
 * {@link UriComponentsBuilder} to construct correct queries.
 */
@RequiredArgsConstructor
public class DefaultMusicBrainz implements MusicBrainz {

    private final MusicBrainzWebClient webClient;

    @Override
    public <T> Mono<T> lookup(EntityType entity, String mbid, Class<T> responseType, long includeMask) {
        String uri = UriComponentsBuilder.newInstance()
                .pathSegment(entity.getValue(), mbid)
                .queryParamIfPresent("inc", buildIncParam(includeMask))
                .queryParam("fmt", "json")
                .build()
                .toUriString();

        return webClient.get(uri, responseType);
    }

    @Override
    public <T> Mono<T> lookupByIsrc(String isrc, Class<T> responseType, long includeMask) {
        String uri = UriComponentsBuilder.newInstance()
                .pathSegment("isrc", isrc)
                .queryParamIfPresent("inc", buildIncParam(includeMask))
                .queryParam("fmt", "json")
                .build()
                .toUriString();

        return webClient.get(uri, responseType);
    }

    @Override
    public <T> Mono<T> lookupByDiscid(String discId, Class<T> responseType, long includeMask) {
        String uri = UriComponentsBuilder.newInstance()
                .pathSegment("discid", discId)
                .queryParamIfPresent("inc", buildIncParam(includeMask))
                .queryParam("fmt", "json")
                .build()
                .toUriString();

        return webClient.get(uri, responseType);
    }

    private java.util.Optional<String> buildIncParam(long includeMask) {
        if (includeMask == 0) {
            return java.util.Optional.empty();
        }

        String[] incs = com.ggar.hibiki.packages.musicbrainz.model.Include.extractIncludes(includeMask);
        return java.util.Optional.of(String.join("+", incs));
    }
}
