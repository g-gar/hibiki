package com.ggar.hibiki.features.ingestion.pipeline;

import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Orchestrator that discovers all IngestionStage beans, orders them,
 * and executes them sequentially.
 */
@Slf4j
@Component
public class IngestionPipeline {

    private final List<IngestionStage> stages;

    public IngestionPipeline(List<IngestionStage> stages) {
        this.stages = stages.stream()
                .sorted(Comparator.comparingInt(IngestionStage::order))
                .toList();
        log.info("Ingestion pipeline initialized with {} stages", this.stages.size());
    }

    public Mono<IngestionContext> execute(IngestionContext context) {
        Mono<IngestionContext> pipeline = Mono.just(context);

        for (IngestionStage stage : stages) {
            pipeline = pipeline.flatMap(ctx -> {
                if (stage.supports(ctx)) {
                    log.debug("Executing stage: {}", stage.getClass().getSimpleName());
                    return stage.process(ctx);
                }
                return Mono.just(ctx);
            });
        }

        return pipeline;
    }
}
