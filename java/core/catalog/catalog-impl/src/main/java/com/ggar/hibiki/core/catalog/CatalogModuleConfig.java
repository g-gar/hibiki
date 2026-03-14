package com.ggar.hibiki.core.catalog;

import com.ggar.hibiki.core.catalog.persistence.entity.AlbumEntity;
import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import com.ggar.hibiki.core.catalog.persistence.entity.SongEntity;
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
@ComponentScan(basePackages = "com.ggar.hibiki.core.catalog")
@EnableReactiveNeo4jRepositories(
        basePackages = "com.ggar.hibiki.core.catalog.persistence.repository",
        neo4jMappingContextRef = "catalogNeo4jMappingContext",
        neo4jTemplateRef = "catalogReactiveNeo4jTemplate")
public class CatalogModuleConfig {

    @Bean(name = "catalogNeo4jMappingContext")
    public Neo4jMappingContext neo4jMappingContext() {
        Neo4jMappingContext context = new Neo4jMappingContext();
        context.setInitialEntitySet(Set.of(ArtistEntity.class, AlbumEntity.class, SongEntity.class));
        return context;
    }

    @Bean(name = "catalogReactiveNeo4jTemplate")
    public ReactiveNeo4jTemplate catalogReactiveNeo4jTemplate(
            ReactiveNeo4jClient neo4jClient,
            @Qualifier("catalogNeo4jMappingContext") Neo4jMappingContext mappingContext,
            ReactiveTransactionManager reactiveTransactionManager) {
        return new ReactiveNeo4jTemplate(
                neo4jClient, mappingContext, (ReactiveNeo4jTransactionManager) reactiveTransactionManager);
    }
}
