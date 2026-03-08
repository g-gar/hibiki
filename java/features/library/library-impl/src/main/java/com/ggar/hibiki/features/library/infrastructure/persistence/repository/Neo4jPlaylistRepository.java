package com.ggar.hibiki.features.library.infrastructure.persistence.repository;

import com.ggar.hibiki.features.library.infrastructure.persistence.access.AccessIntentTranslator;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.PlaylistEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.mapper.LibraryMapper;
import com.ggar.hibiki.features.library.model.AccessIntent;
import com.ggar.hibiki.features.library.model.LibraryFilter;
import com.ggar.hibiki.features.library.model.Pagination;
import com.ggar.hibiki.features.library.model.Playlist;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.port.LibraryAccessContributor;
import com.ggar.hibiki.features.library.port.PlaylistRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Low-level SDN repository for PlaylistEntity.
 */
interface Neo4jPlaylistEntityRepository extends ReactiveNeo4jRepository<PlaylistEntity, UUID> {}

/**
 * Implementation of PlaylistRepository port using Spring Data Neo4j.
 */
@Repository
@RequiredArgsConstructor
public class Neo4jPlaylistRepository implements PlaylistRepository {

    private final Neo4jPlaylistEntityRepository playlistRepository;
    private final LibraryMapper mapper;
    private final ReactiveNeo4jClient neo4jClient;
    private final List<LibraryAccessContributor> accessContributors;
    private final List<AccessIntentTranslator> translators;

    @Override
    public Mono<Playlist> save(User user, Playlist playlist) {
        if (!playlist.getUser().getId().equals(user.getId())) {
            return Mono.error(new IllegalArgumentException("Playlist does not belong to the user context"));
        }
        PlaylistEntity entity = mapper.toEntity(playlist);
        return playlistRepository.save(entity).map(mapper::toDomain);
    }

    @Override
    public Mono<Playlist> findById(User user, UUID playlistId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", user.getId().toString());
        parameters.put("playlistId", playlistId.toString());

        List<AccessIntent> allIntents = accessContributors.stream()
                .flatMap(c -> c.getAccessIntents(user).stream())
                .toList();

        StringBuilder expansions = new StringBuilder();
        for (AccessIntent intent : allIntents) {
            if (intent.getType() == AccessIntent.Type.EXPANSION) {
                translateIntent(intent, parameters)
                        .ifPresent(fragment -> expansions.append(" UNION ").append(fragment));
            }
        }

        StringBuilder restrictions = new StringBuilder();
        for (AccessIntent intent : allIntents) {
            if (intent.getType() == AccessIntent.Type.RESTRICTION) {
                translateIntent(intent, parameters).ifPresent(fragment -> {
                    if (restrictions.length() > 0) restrictions.append(" AND ");
                    restrictions.append("(").append(fragment).append(")");
                });
            }
        }

        String query =
                """
                MATCH (u:User {id: $userId})
                CALL {
                    WITH u
                    MATCH (u)-[:HAS_IN_LIBRARY]->(li:Playlist {id: $playlistId})
                    RETURN li, labels(li) as labels
                    %s
                }
                WITH li, labels
                %s
                RETURN li, labels
                LIMIT 1
                """
                        .formatted(
                                expansions.toString(),
                                restrictions.length() > 0 ? "WHERE " + restrictions.toString() : "");

        return neo4jClient.query(query).bindAll(parameters).fetch().one().map(row ->
                (Playlist) mapper.toDomain((PlaylistEntity) row.get("li")));
    }

    @Override
    public Mono<Void> delete(User user, UUID playlistId) {
        // Enforce access control on delete
        return findById(user, playlistId).flatMap(p -> playlistRepository.deleteById(p.getId()));
    }

    @Override
    public Flux<Playlist> findAll(User user, LibraryFilter filter, Pagination pagination) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", user.getId().toString());
        parameters.put("skip", pagination.getPage() * pagination.getSize());
        parameters.put("limit", pagination.getSize());

        List<AccessIntent> allIntents = accessContributors.stream()
                .flatMap(c -> c.getAccessIntents(user).stream())
                .toList();

        StringBuilder expansions = new StringBuilder();
        for (AccessIntent intent : allIntents) {
            if (intent.getType() == AccessIntent.Type.EXPANSION) {
                translateIntent(intent, parameters)
                        .ifPresent(fragment -> expansions.append(" UNION ").append(fragment));
            }
        }

        StringBuilder restrictions = new StringBuilder();
        for (AccessIntent intent : allIntents) {
            if (intent.getType() == AccessIntent.Type.RESTRICTION) {
                translateIntent(intent, parameters).ifPresent(fragment -> {
                    if (restrictions.length() > 0) restrictions.append(" AND ");
                    restrictions.append("(").append(fragment).append(")");
                });
            }
        }

        String query =
                """
                MATCH (u:User {id: $userId})
                CALL {
                    WITH u
                    MATCH (u)-[:HAS_IN_LIBRARY]->(li:Playlist)
                    RETURN li, li.addedAt as sortKey, labels(li) as labels
                    %s
                }
                WITH li, labels, sortKey
                %s
                RETURN li, labels
                ORDER BY sortKey DESC
                SKIP $skip LIMIT $limit
                """
                        .formatted(
                                expansions.toString(),
                                restrictions.length() > 0 ? "WHERE " + restrictions.toString() : "");

        return neo4jClient.query(query).bindAll(parameters).fetch().all().map(row ->
                (Playlist) mapper.toDomain((PlaylistEntity) row.get("li")));
    }

    @Override
    public Mono<Long> count(User user, LibraryFilter filter) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", user.getId().toString());

        List<AccessIntent> allIntents = accessContributors.stream()
                .flatMap(c -> c.getAccessIntents(user).stream())
                .toList();

        StringBuilder expansions = new StringBuilder();
        for (AccessIntent intent : allIntents) {
            if (intent.getType() == AccessIntent.Type.EXPANSION) {
                translateIntent(intent, parameters)
                        .ifPresent(fragment -> expansions.append(" UNION ").append(fragment));
            }
        }

        StringBuilder restrictions = new StringBuilder();
        for (AccessIntent intent : allIntents) {
            if (intent.getType() == AccessIntent.Type.RESTRICTION) {
                translateIntent(intent, parameters).ifPresent(fragment -> {
                    if (restrictions.length() > 0) restrictions.append(" AND ");
                    restrictions.append("(").append(fragment).append(")");
                });
            }
        }

        String query =
                """
                MATCH (u:User {id: $userId})
                CALL {
                    WITH u
                    MATCH (u)-[:HAS_IN_LIBRARY]->(li:Playlist)
                    RETURN li
                    %s
                }
                WITH li
                %s
                RETURN count(li) as total
                """
                        .formatted(
                                expansions.toString(),
                                restrictions.length() > 0 ? "WHERE " + restrictions.toString() : "");

        return neo4jClient
                .query(query)
                .bindAll(parameters)
                .fetchAs(Long.class)
                .one()
                .defaultIfEmpty(0L);
    }

    private Optional<String> translateIntent(AccessIntent intent, Map<String, Object> parameters) {
        return translators.stream()
                .filter(t -> t.supports(intent.getId()))
                .findFirst()
                .flatMap(t -> t.translate(intent, parameters));
    }
}
