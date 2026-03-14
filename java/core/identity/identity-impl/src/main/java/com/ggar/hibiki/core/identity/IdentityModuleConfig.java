package com.ggar.hibiki.core.identity;

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
@ComponentScan(basePackages = "com.ggar.hibiki.core.identity")
@EnableReactiveNeo4jRepositories(
        basePackages = "com.ggar.hibiki.core.identity.persistence.repository",
        neo4jMappingContextRef = "identityNeo4jMappingContext",
        neo4jTemplateRef = "identityReactiveNeo4jTemplate")
public class IdentityModuleConfig {

    @Bean(name = "identityNeo4jMappingContext")
    public Neo4jMappingContext identityNeo4jMappingContext() {
        Neo4jMappingContext context = new Neo4jMappingContext();
        context.getPersistentEntity(com.ggar.hibiki.core.identity.persistence.entity.UserEntity.class);
        return context;
    }

    @Bean(name = "identityReactiveNeo4jTemplate")
    public ReactiveNeo4jTemplate identityReactiveNeo4jTemplate(
            ReactiveNeo4jClient neo4jClient,
            @Qualifier("identityNeo4jMappingContext") Neo4jMappingContext identityNeo4jMappingContext,
            ReactiveTransactionManager reactiveTransactionManager) {
        return new ReactiveNeo4jTemplate(neo4jClient, identityNeo4jMappingContext, (ReactiveNeo4jTransactionManager)
                reactiveTransactionManager);
    }
}
