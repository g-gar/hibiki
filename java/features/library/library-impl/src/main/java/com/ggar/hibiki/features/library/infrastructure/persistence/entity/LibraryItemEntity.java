package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import com.ggar.hibiki.features.library.infrastructure.persistence.generator.UuidV7IdGenerator;
import com.ggar.hibiki.features.library.model.Visibility;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Base abstract node for all library items.
 * Uses SDN inheritance mapping (multiple labels).
 */
@Node("LibraryItem")
public abstract class LibraryItemEntity {

    @Id
    @GeneratedValue(UuidV7IdGenerator.class)
    private UUID id;

    @Relationship(type = "HAS_IN_LIBRARY", direction = Relationship.Direction.INCOMING)
    private UserEntity user;

    @Property("visibility")
    private Visibility visibility;

    @Property("owner")
    private boolean owner;

    @Property("addedAt")
    private Instant addedAt;

    protected LibraryItemEntity() {}

    protected LibraryItemEntity(UUID id, UserEntity user, Visibility visibility, boolean owner, Instant addedAt) {
        this.id = id;
        this.user = user;
        this.visibility = visibility;
        this.owner = owner;
        this.addedAt = addedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public Instant getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Instant addedAt) {
        this.addedAt = addedAt;
    }
}
