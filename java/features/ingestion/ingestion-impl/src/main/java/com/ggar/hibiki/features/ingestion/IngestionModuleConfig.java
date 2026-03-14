package com.ggar.hibiki.features.ingestion;

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
@ComponentScan(basePackages = "com.ggar.hibiki.features.ingestion")
@EnableReactiveNeo4jRepositories(
        basePackages = "com.ggar.hibiki.features.ingestion.infrastructure.persistence.repository",
        neo4jMappingContextRef = "ingestionNeo4jMappingContext",
        neo4jTemplateRef = "ingestionReactiveNeo4jTemplate")
public class IngestionModuleConfig {

    @Bean(name = "ingestionNeo4jMappingContext")
    public Neo4jMappingContext ingestionNeo4jMappingContext() {
        Neo4jMappingContext context = new Neo4jMappingContext();
        context.getPersistentEntity(
                com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.UserEntity.class);
        return context;
    }

    @Bean(name = "ingestionReactiveNeo4jTemplate")
    public ReactiveNeo4jTemplate ingestionReactiveNeo4jTemplate(
            ReactiveNeo4jClient neo4jClient,
            @Qualifier("ingestionNeo4jMappingContext") Neo4jMappingContext ingestionNeo4jMappingContext,
            ReactiveTransactionManager reactiveTransactionManager) {
        return new ReactiveNeo4jTemplate(neo4jClient, ingestionNeo4jMappingContext, (ReactiveNeo4jTransactionManager)
                reactiveTransactionManager);
    }
}
