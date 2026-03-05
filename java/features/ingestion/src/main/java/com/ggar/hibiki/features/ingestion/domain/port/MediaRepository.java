package com.ggar.hibiki.features.ingestion.domain.port;

import com.ggar.hibiki.features.ingestion.domain.Media;
import reactor.core.publisher.Mono;

public interface MediaRepository {
    Mono<Media> save(Media media);
}
