package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import java.time.Instant;
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
    @GeneratedValue
    private String id;

    @Property("position")
    private Integer position;

    @Property("addedAt")
    private Instant addedAt;

    @TargetNode
    private SongEntity song;

    public PlaylistItemRelationship() {}

    public PlaylistItemRelationship(String id, Integer position, Instant addedAt, SongEntity song) {
        this.id = id;
        this.position = position;
        this.addedAt = addedAt;
        this.song = song;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        private String id;
        private Integer position;
        private Instant addedAt;
        private SongEntity song;

        public PlaylistItemRelationshipBuilder id(String id) {
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
