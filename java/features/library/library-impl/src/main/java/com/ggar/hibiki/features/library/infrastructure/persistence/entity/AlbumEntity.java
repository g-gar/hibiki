package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import java.util.UUID;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Lightweight Album entity for library relationships.
 */
@Node("Album")
public class AlbumEntity {

    @Id
    private UUID id;

    @Relationship(type = "CREATED_BY", direction = Relationship.Direction.OUTGOING)
    private ArtistEntity artist;

    public AlbumEntity() {}

    public AlbumEntity(UUID id, ArtistEntity artist) {
        this.id = id;
        this.artist = artist;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ArtistEntity getArtist() {
        return artist;
    }

    public void setArtist(ArtistEntity artist) {
        this.artist = artist;
    }

    public static AlbumEntityBuilder builder() {
        return new AlbumEntityBuilder();
    }

    public static class AlbumEntityBuilder {
        private UUID id;
        private ArtistEntity artist;

        public AlbumEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AlbumEntityBuilder artist(ArtistEntity artist) {
            this.artist = artist;
            return this;
        }

        public AlbumEntity build() {
            return new AlbumEntity(id, artist);
        }
    }
}
