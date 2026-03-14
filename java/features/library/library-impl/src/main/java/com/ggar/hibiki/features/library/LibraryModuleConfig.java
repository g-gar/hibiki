package com.ggar.hibiki.features.library;

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
@ComponentScan(basePackages = "com.ggar.hibiki.features.library")
@EnableReactiveNeo4jRepositories(
        basePackages = "com.ggar.hibiki.features.library.infrastructure.persistence.repository",
        neo4jMappingContextRef = "libraryNeo4jMappingContext",
        neo4jTemplateRef = "libraryReactiveNeo4jTemplate")
public class LibraryModuleConfig {

    @Bean(name = "libraryNeo4jMappingContext")
    public Neo4jMappingContext libraryNeo4jMappingContext() {
        Neo4jMappingContext context = new Neo4jMappingContext();
        context.setInitialEntitySet(Set.of(
                com.ggar.hibiki.features.library.infrastructure.persistence.entity.ArtistEntity.class,
                com.ggar.hibiki.features.library.infrastructure.persistence.entity.AlbumEntity.class,
                com.ggar.hibiki.features.library.infrastructure.persistence.entity.SongEntity.class,
                com.ggar.hibiki.features.library.infrastructure.persistence.entity.UserEntity.class,
                com.ggar.hibiki.features.library.infrastructure.persistence.entity.LibraryItemEntity.class,
                com.ggar.hibiki.features.library.infrastructure.persistence.entity.SongLibraryItemEntity.class,
                com.ggar.hibiki.features.library.infrastructure.persistence.entity.PlaylistItemRelationship.class));
        return context;
    }

    @Bean(name = "libraryReactiveNeo4jTemplate")
    public ReactiveNeo4jTemplate libraryReactiveNeo4jTemplate(
            ReactiveNeo4jClient neo4jClient,
            @Qualifier("libraryNeo4jMappingContext") Neo4jMappingContext libraryNeo4jMappingContext,
            ReactiveTransactionManager reactiveTransactionManager) {
        return new ReactiveNeo4jTemplate(neo4jClient, libraryNeo4jMappingContext, (ReactiveNeo4jTransactionManager)
                reactiveTransactionManager);
    }
}
