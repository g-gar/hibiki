package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import com.ggar.hibiki.features.library.model.Visibility;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Concrete entity for Playlists.
 */
@Node("Playlist")
public class PlaylistEntity extends LibraryItemEntity {

    @Property("name")
    private String name;

    @Property("description")
    private String description;

    @Property("updatedAt")
    private Instant updatedAt;

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    private List<PlaylistItemRelationship> items;

    public PlaylistEntity() {}

    public PlaylistEntity(
            UUID id,
            UserEntity user,
            Visibility visibility,
            boolean owner,
            Instant addedAt,
            String name,
            String description,
            Instant updatedAt,
            List<PlaylistItemRelationship> items) {
        super(id, user, visibility, owner, addedAt);
        this.name = name;
        this.description = description;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<PlaylistItemRelationship> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItemRelationship> items) {
        this.items = items;
    }

    public static PlaylistEntityBuilder builder() {
        return new PlaylistEntityBuilder();
    }

    public static class PlaylistEntityBuilder {
        private UUID id;
        private UserEntity user;
        private Visibility visibility;
        private boolean owner;
        private Instant addedAt;
        private String name;
        private String description;
        private Instant updatedAt;
        private List<PlaylistItemRelationship> items;

        public PlaylistEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PlaylistEntityBuilder user(UserEntity user) {
            this.user = user;
            return this;
        }

        public PlaylistEntityBuilder visibility(Visibility visibility) {
            this.visibility = visibility;
            return this;
        }

        public PlaylistEntityBuilder owner(boolean owner) {
            this.owner = owner;
            return this;
        }

        public PlaylistEntityBuilder addedAt(Instant addedAt) {
            this.addedAt = addedAt;
            return this;
        }

        public PlaylistEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PlaylistEntityBuilder description(String description) {
            this.description = description;
            return this;
        }

        public PlaylistEntityBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PlaylistEntityBuilder items(List<PlaylistItemRelationship> items) {
            this.items = items;
            return this;
        }

        public PlaylistEntity build() {
            return new PlaylistEntity(id, user, visibility, owner, addedAt, name, description, updatedAt, items);
        }
    }
}
