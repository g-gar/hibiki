package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import java.util.UUID;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Lightweight Artist entity for library relationships.
 */
@Node("Artist")
public class ArtistEntity {

    @Id
    private UUID id;

    public ArtistEntity() {}

    public ArtistEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public static ArtistEntityBuilder builder() {
        return new ArtistEntityBuilder();
    }

    public static class ArtistEntityBuilder {
        private UUID id;

        public ArtistEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ArtistEntity build() {
            return new ArtistEntity(id);
        }
    }
}
