package com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node("User")
public class UserEntity {

    @Id
    private UUID id;

    public UserEntity() {
    }

    public UserEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
