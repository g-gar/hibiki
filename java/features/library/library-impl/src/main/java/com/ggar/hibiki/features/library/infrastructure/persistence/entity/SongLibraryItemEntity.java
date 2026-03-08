package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import com.ggar.hibiki.features.library.model.Visibility;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Concrete entity for Songs in library.
 */
@Node("SongLibraryItem")
public class SongLibraryItemEntity extends LibraryItemEntity {

    @Relationship(type = "REFERENCES", direction = Relationship.Direction.OUTGOING)
    private SongEntity song;

    public SongLibraryItemEntity() {}

    public SongLibraryItemEntity(
            UUID id, UserEntity user, Visibility visibility, boolean owner, Instant addedAt, SongEntity song) {
        super(id, user, visibility, owner, addedAt);
        this.song = song;
    }

    public SongEntity getSong() {
        return song;
    }

    public void setSong(SongEntity song) {
        this.song = song;
    }

    public static SongLibraryItemEntityBuilder builder() {
        return new SongLibraryItemEntityBuilder();
    }

    public static class SongLibraryItemEntityBuilder {
        private UUID id;
        private UserEntity user;
        private Visibility visibility;
        private boolean owner;
        private Instant addedAt;
        private SongEntity song;

        public SongLibraryItemEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public SongLibraryItemEntityBuilder user(UserEntity user) {
            this.user = user;
            return this;
        }

        public SongLibraryItemEntityBuilder visibility(Visibility visibility) {
            this.visibility = visibility;
            return this;
        }

        public SongLibraryItemEntityBuilder owner(boolean owner) {
            this.owner = owner;
            return this;
        }

        public SongLibraryItemEntityBuilder addedAt(Instant addedAt) {
            this.addedAt = addedAt;
            return this;
        }

        public SongLibraryItemEntityBuilder song(SongEntity song) {
            this.song = song;
            return this;
        }

        public SongLibraryItemEntity build() {
            return new SongLibraryItemEntity(id, user, visibility, owner, addedAt, song);
        }
    }
}
