package com.ggar.hibiki.features.library.infrastructure.persistence.repository;

import com.ggar.hibiki.features.library.infrastructure.persistence.entity.LibraryItemEntity;
import java.util.UUID;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repository for polymorphic LibraryItem entities.
 */
@Repository
public interface Neo4jLibraryItemRepository extends ReactiveNeo4jRepository<LibraryItemEntity, UUID> {

    @Query("MATCH (u:User {id: $userId})-[:HAS_IN_LIBRARY]->(li:LibraryItem) "
            + "WHERE (li)-[:REFERENCES]->({id: $mediaId}) OR li.id = $mediaId "
            + "RETURN li")
    Mono<LibraryItemEntity> findByUserAndMediaId(UUID userId, UUID mediaId);
}
