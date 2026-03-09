package com.ggar.hibiki.features.ingestion.pipeline;

import reactor.core.publisher.Mono;

/**
 * SPI for extensible processing stages in the ingestion pipeline.
 * Implementations are registered as Spring @Component beans and
 * automatically discovered and ordered by the pipeline orchestrator.
 */
public interface IngestionStage {

    /**
     * Determines execution order. Lower values execute first.
     */
    int order();

    /**
     * Whether this stage supports processing the given context.
     */
    boolean supports(IngestionContext context);

    /**
     * Processes the context and returns a potentially modified version.
     */
    Mono<IngestionContext> process(IngestionContext context);
}
