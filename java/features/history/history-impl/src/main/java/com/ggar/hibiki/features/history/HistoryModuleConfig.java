package com.ggar.hibiki.features.history;

import com.ggar.hibiki.features.history.infrastructure.persistence.entity.PlaybackHistoryEntity;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration
@ComponentScan(basePackages = "com.ggar.hibiki.features.history")
@EnableReactiveNeo4jRepositories(
        basePackages = "com.ggar.hibiki.features.history.infrastructure.persistence.repository",
        neo4jMappingContextRef = "historyNeo4jMappingContext",
        neo4jTemplateRef = "historyReactiveNeo4jTemplate")
public class HistoryModuleConfig {

    @Bean(name = "historyNeo4jMappingContext")
    public Neo4jMappingContext historyNeo4jMappingContext() {
        Neo4jMappingContext context = new Neo4jMappingContext();
        context.setInitialEntitySet(Set.of(PlaybackHistoryEntity.class));
        return context;
    }

    @Bean(name = "historyReactiveNeo4jTemplate")
    public ReactiveNeo4jTemplate historyReactiveNeo4jTemplate(
            ReactiveNeo4jClient neo4jClient,
            @Qualifier("historyNeo4jMappingContext") Neo4jMappingContext historyNeo4jMappingContext,
            ReactiveTransactionManager reactiveTransactionManager) {
        return new ReactiveNeo4jTemplate(
                neo4jClient, historyNeo4jMappingContext, (ReactiveNeo4jTransactionManager) reactiveTransactionManager);
    }
}
