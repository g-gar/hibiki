package com.ggar.hibiki.features.library.infrastructure.persistence.repository;

import com.ggar.hibiki.features.library.infrastructure.persistence.access.AccessIntentTranslator;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.AlbumEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.entity.LibraryItemEntity;
import com.ggar.hibiki.features.library.infrastructure.persistence.mapper.LibraryMapper;
import com.ggar.hibiki.features.library.model.AccessIntent;
import com.ggar.hibiki.features.library.model.AlbumLibraryItem;
import com.ggar.hibiki.features.library.model.LibraryFilter;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemId;
import com.ggar.hibiki.features.library.model.Pagination;
import com.ggar.hibiki.features.library.model.User;
import com.ggar.hibiki.features.library.model.Visibility;
import com.ggar.hibiki.features.library.port.LibraryAccessContributor;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of LibraryRepository port using Spring Data Neo4j.
 */
@Repository
@RequiredArgsConstructor
public class Neo4jLibraryRepository implements LibraryRepository {

    private final Neo4jLibraryItemRepository itemRepository;
    private final LibraryMapper mapper;
    private final ReactiveNeo4jClient neo4jClient;
    private final List<LibraryAccessContributor> accessContributors;
    private final List<AccessIntentTranslator> translators;

    @Override
    public Mono<LibraryItem> save(User user, LibraryItem item) {
        if (!item.getUser().getId().equals(user.getId())) {
            return Mono.error(new IllegalArgumentException("Library item does not belong to the user context"));
        }
        LibraryItemEntity entity = mapper.toEntity(item);
        return itemRepository.save(entity).map(mapper::toDomain);
    }

    @Override
    public Mono<Void> remove(User user, UUID mediaId) {
        // We use a custom query because mediaId can be either:
        // 1. The ID of the Referenced node (Song/Album)
        // 2. The ID of the LibraryItem itself (Playlist)
        String cypher =
                """
                MATCH (u:User {id: $userId})-[r:HAS_IN_LIBRARY]->(li:LibraryItem)
                WHERE (li)-[:REFERENCES]->({id: $mediaId}) OR li.id = $mediaId
                DETACH DELETE li
                """;

        return neo4jClient
                .query(cypher)
                .bind(user.getId().getValue().toString())
                .to("userId")
                .bind(mediaId.toString())
                .to("mediaId")
                .run()
                .then();
    }

    @Override
    public Mono<LibraryItem> findById(User user, UUID mediaId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", user.getId().getValue().toString());
        parameters.put("mediaId", mediaId.toString());

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
                    MATCH (u)-[:HAS_IN_LIBRARY]->(li:LibraryItem)
                    WHERE (li)-[:REFERENCES]->({id: $mediaId}) OR li.id = $mediaId
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

        return neo4jClient.query(query).bindAll(parameters).fetch().one().map(row -> mapRowToLibraryItem(row, user));
    }

    @Override
    public Mono<Boolean> exists(User user, UUID mediaId) {
        String cypher =
                """
                MATCH (u:User {id: $userId})-[r:HAS_IN_LIBRARY]->(li:LibraryItem)
                WHERE (li)-[:REFERENCES]->({id: $mediaId}) OR li.id = $mediaId
                RETURN count(li) > 0
                """;

        return neo4jClient
                .query(cypher)
                .bind(user.getId().getValue().toString())
                .to("userId")
                .bind(mediaId.toString())
                .to("mediaId")
                .fetchAs(Boolean.class)
                .one()
                .defaultIfEmpty(false);
    }

    @Override
    public Flux<LibraryItem> findAll(User user, LibraryFilter filter, Pagination pagination) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", user.getId().getValue().toString());
        parameters.put(
                "types",
                filter.getTypes() != null
                        ? filter.getTypes().stream().map(Enum::name).toList()
                        : null);
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
                    MATCH (u)-[:HAS_IN_LIBRARY]->(li:LibraryItem)
                    // Basic type filtering
                    WHERE ($types IS NULL OR size($types) = 0 OR any(t IN $types WHERE li:_type = t))
                    RETURN li, li.addedAt as sortKey, labels(li) as labels
                    UNION
                    WITH u
                    MATCH (u)-[:HAS_IN_LIBRARY]->(:SongLibraryItem)-[:REFERENCES]->(:Song)-[:PART_OF]->(a:Album)
                    WHERE NOT (u)-[:HAS_IN_LIBRARY]->(:AlbumLibraryItem)-[:REFERENCES]->(a)
                    AND ($types IS NULL OR size($types) = 0 OR 'ALBUM' IN $types)
                    RETURN a as li, min(datetime()) as sortKey, ['Album', 'Virtual'] as labels
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

        return neo4jClient.query(query).bindAll(parameters).fetch().all().map(row -> mapRowToLibraryItem(row, user));
    }

    @Override
    public Mono<Long> count(User user, LibraryFilter filter) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", user.getId().getValue().toString());
        parameters.put(
                "types",
                filter.getTypes() != null
                        ? filter.getTypes().stream().map(Enum::name).toList()
                        : null);

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
                    MATCH (u)-[:HAS_IN_LIBRARY]->(li:LibraryItem)
                    WHERE ($types IS NULL OR size($types) = 0 OR any(t IN $types WHERE li:_type = t))
                    RETURN li
                    UNION
                    WITH u
                    MATCH (u)-[:HAS_IN_LIBRARY]->(:SongLibraryItem)-[:REFERENCES]->(:Song)-[:PART_OF]->(a:Album)
                    WHERE NOT (u)-[:HAS_IN_LIBRARY]->(:AlbumLibraryItem)-[:REFERENCES]->(a)
                    AND ($types IS NULL OR size($types) = 0 OR 'ALBUM' IN $types)
                    RETURN a as li
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

    @Override
    public Mono<Void> removeByArtistId(UUID artistId) {
        String cypher =
                """
                MATCH (li:LibraryItem)-[:REFERENCES]->(ref)
                WHERE (ref:Artist AND ref.id = $artistId)
                   OR EXISTS { (ref:Album)-[:BY_ARTIST]->(:Artist {id: $artistId}) }
                   OR EXISTS { (ref:Song)-[:BY_ARTIST]->(:Artist {id: $artistId}) }
                   OR EXISTS { (ref:Song)-[:PART_OF]->(:Album)-[:BY_ARTIST]->(:Artist {id: $artistId}) }
                DETACH DELETE li
                """;
        return neo4jClient
                .query(cypher)
                .bind(artistId.toString())
                .to("artistId")
                .run()
                .then();
    }

    @Override
    public Mono<Void> removeBySongId(UUID songId) {
        String cypher =
                """
                MATCH (li:LibraryItem)-[:REFERENCES]->(:Song {id: $songId})
                DETACH DELETE li
                """;
        return neo4jClient
                .query(cypher)
                .bind(songId.toString())
                .to("songId")
                .run()
                .then();
    }

    @Override
    public Mono<Void> removeByAlbumId(UUID albumId) {
        String cypher =
                """
                MATCH (li:LibraryItem)-[:REFERENCES]->(ref)
                WHERE (ref:Album AND ref.id = $albumId)
                   OR EXISTS { (ref:Song)-[:PART_OF]->(:Album {id: $albumId}) }
                DETACH DELETE li
                """;
        return neo4jClient
                .query(cypher)
                .bind(albumId.toString())
                .to("albumId")
                .run()
                .then();
    }

    private Optional<String> translateIntent(AccessIntent intent, Map<String, Object> parameters) {
        return translators.stream()
                .filter(t -> t.supports(intent.getId()))
                .findFirst()
                .flatMap(t -> t.translate(intent, parameters));
    }

    @SuppressWarnings("unchecked")
    private LibraryItem mapRowToLibraryItem(Map<String, Object> row, User user) {
        Object li = row.get("li");
        List<String> labels = (List<String>) row.get("labels");

        if (labels.contains("Virtual")) {
            // It's an Album node being treated as a virtual AlbumLibraryItem
            // For simplicity, we create a domain AlbumLibraryItem directly
            // In a more complex setup, we'd use the mapper on a reconstructed entity
            AlbumEntity albumEntity = (AlbumEntity) li;
            return AlbumLibraryItem.builder()
                    .id(LibraryItemId.of(albumEntity.getId()))
                    .user(user)
                    .visibility(Visibility.PRIVATE)
                    .owner(false)
                    .addedAt(Instant.now())
                    .album(mapper.toDomain(albumEntity))
                    .build();
        } else {
            return mapper.toDomain((LibraryItemEntity) li);
        }
    }
}
