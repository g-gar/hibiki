package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import com.ggar.hibiki.features.library.model.Visibility;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Concrete entity for Albums in library.
 */
@Node("AlbumLibraryItem")
public class AlbumLibraryItemEntity extends LibraryItemEntity {

    @Relationship(type = "REFERENCES", direction = Relationship.Direction.OUTGOING)
    private AlbumEntity album;

    public AlbumLibraryItemEntity() {}

    public AlbumLibraryItemEntity(
            UUID id, UserEntity user, Visibility visibility, boolean owner, Instant addedAt, AlbumEntity album) {
        super(id, user, visibility, owner, addedAt);
        this.album = album;
    }

    public AlbumEntity getAlbum() {
        return album;
    }

    public void setAlbum(AlbumEntity album) {
        this.album = album;
    }

    public static AlbumLibraryItemEntityBuilder builder() {
        return new AlbumLibraryItemEntityBuilder();
    }

    public static class AlbumLibraryItemEntityBuilder {
        private UUID id;
        private UserEntity user;
        private Visibility visibility;
        private boolean owner;
        private Instant addedAt;
        private AlbumEntity album;

        public AlbumLibraryItemEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AlbumLibraryItemEntityBuilder user(UserEntity user) {
            this.user = user;
            return this;
        }

        public AlbumLibraryItemEntityBuilder visibility(Visibility visibility) {
            this.visibility = visibility;
            return this;
        }

        public AlbumLibraryItemEntityBuilder owner(boolean owner) {
            this.owner = owner;
            return this;
        }

        public AlbumLibraryItemEntityBuilder addedAt(Instant addedAt) {
            this.addedAt = addedAt;
            return this;
        }

        public AlbumLibraryItemEntityBuilder album(AlbumEntity album) {
            this.album = album;
            return this;
        }

        public AlbumLibraryItemEntity build() {
            return new AlbumLibraryItemEntity(id, user, visibility, owner, addedAt, album);
        }
    }
}
