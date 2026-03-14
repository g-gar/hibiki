package com.ggar.hibiki.test.integration.real.config;

import com.ggar.hibiki.test.support.CapturingEventBus;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration
public class Neo4jTestSharedConfiguration {

    @Bean
    @Primary
    public Driver neo4jDriver(@Value("${spring.neo4j.uri}") String uri) {
        return GraphDatabase.driver(uri, AuthTokens.none());
    }

    @Bean
    @Primary
    public ReactiveNeo4jClient reactiveNeo4jClient(Driver driver) {
        return ReactiveNeo4jClient.create(driver);
    }

    @Bean(name = "reactiveTransactionManager")
    @Primary
    public ReactiveTransactionManager reactiveTransactionManager(Driver driver) {
        return new org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager(driver);
    }

    @Bean
    @Primary
    public CapturingEventBus capturingEventBus() {
        return new CapturingEventBus();
    }
}
