package com.ggar.hibiki.features.library.infrastructure.persistence.entity;

import java.util.UUID;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * Lightweight User entity for library relationships.
 */
@Node("User")
public class UserEntity {

    @Id
    private UUID id;

    public UserEntity() {}

    public UserEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public static UserEntityBuilder builder() {
        return new UserEntityBuilder();
    }

    public static class UserEntityBuilder {
        private UUID id;

        public UserEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(id);
        }
    }
}
