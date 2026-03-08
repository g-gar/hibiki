package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import com.ggar.hibiki.features.library.infrastructure.persistence.generator.UuidV7IdGenerator;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

/**
 * Relationship entity for items within a playlist.
 */
@RelationshipProperties
public class PlaylistItemRelationship {

    @Id
    @GeneratedValue(UuidV7IdGenerator.class)
    private UUID id;

    @Property("position")
    private Integer position;

    @Property("addedAt")
    private Instant addedAt;

    @TargetNode
    private SongEntity song;

    public PlaylistItemRelationship() {}

    public PlaylistItemRelationship(UUID id, Integer position, Instant addedAt, SongEntity song) {
        this.id = id;
        this.position = position;
        this.addedAt = addedAt;
        this.song = song;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Instant getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Instant addedAt) {
        this.addedAt = addedAt;
    }

    public SongEntity getSong() {
        return song;
    }

    public void setSong(SongEntity song) {
        this.song = song;
    }

    public static PlaylistItemRelationshipBuilder builder() {
        return new PlaylistItemRelationshipBuilder();
    }

    public static class PlaylistItemRelationshipBuilder {
        private UUID id;
        private Integer position;
        private Instant addedAt;
        private SongEntity song;

        public PlaylistItemRelationshipBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PlaylistItemRelationshipBuilder position(Integer position) {
            this.position = position;
            return this;
        }

        public PlaylistItemRelationshipBuilder addedAt(Instant addedAt) {
            this.addedAt = addedAt;
            return this;
        }

        public PlaylistItemRelationshipBuilder song(SongEntity song) {
            this.song = song;
            return this;
        }

        public PlaylistItemRelationship build() {
            return new PlaylistItemRelationship(id, position, addedAt, song);
        }
    }
}
