package com.ggar.hibiki.features.devices;

import com.ggar.hibiki.features.devices.infrastructure.persistence.entity.DeviceEntity;
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
@ComponentScan(basePackages = "com.ggar.hibiki.features.devices")
@EnableReactiveNeo4jRepositories(
        basePackages = "com.ggar.hibiki.features.devices.infrastructure.persistence.repository",
        neo4jMappingContextRef = "devicesNeo4jMappingContext",
        neo4jTemplateRef = "devicesReactiveNeo4jTemplate")
public class DevicesModuleConfig {

    @Bean(name = "devicesNeo4jMappingContext")
    public Neo4jMappingContext devicesNeo4jMappingContext() {
        Neo4jMappingContext context = new Neo4jMappingContext();
        context.setInitialEntitySet(Set.of(DeviceEntity.class));
        return context;
    }

    @Bean(name = "devicesReactiveNeo4jTemplate")
    public ReactiveNeo4jTemplate devicesReactiveNeo4jTemplate(
            ReactiveNeo4jClient neo4jClient,
            @Qualifier("devicesNeo4jMappingContext") Neo4jMappingContext devicesNeo4jMappingContext,
            ReactiveTransactionManager reactiveTransactionManager) {
        return new ReactiveNeo4jTemplate(
                neo4jClient, devicesNeo4jMappingContext, (ReactiveNeo4jTransactionManager) reactiveTransactionManager);
    }
}
