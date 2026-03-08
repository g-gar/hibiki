package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import java.util.List;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Lightweight Song entity for library relationships.
 */
@Node("Song")
public class SongEntity {

    @Id
    private UUID id;

    @Relationship(type = "PART_OF", direction = Relationship.Direction.OUTGOING)
    private AlbumEntity album;

    @Relationship(type = "SUNG_BY", direction = Relationship.Direction.OUTGOING)
    private List<ArtistEntity> artists;

    public SongEntity() {}

    public SongEntity(UUID id, AlbumEntity album, List<ArtistEntity> artists) {
        this.id = id;
        this.album = album;
        this.artists = artists;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AlbumEntity getAlbum() {
        return album;
    }

    public void setAlbum(AlbumEntity album) {
        this.album = album;
    }

    public List<ArtistEntity> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistEntity> artists) {
        this.artists = artists;
    }

    public static SongEntityBuilder builder() {
        return new SongEntityBuilder();
    }

    public static class SongEntityBuilder {
        private UUID id;
        private AlbumEntity album;
        private List<ArtistEntity> artists;

        public SongEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public SongEntityBuilder album(AlbumEntity album) {
            this.album = album;
            return this;
        }

        public SongEntityBuilder artists(List<ArtistEntity> artists) {
            this.artists = artists;
            return this;
        }

        public SongEntity build() {
            return new SongEntity(id, album, artists);
        }
    }
}
