package com.ggar.hibiki.features.ingestion.infrastructure.persistence.generator;

import com.ggar.hibiki.packages.uuid.UuidGenerator;
import com.ggar.hibiki.packages.uuid.UuidV7Generator;
import org.springframework.data.neo4j.core.schema.IdGenerator;
import org.springframework.util.Assert;

import java.util.UUID;

public class UuidV7IdGenerator implements IdGenerator<UUID> {

    private final UuidGenerator generator;

    public UuidV7IdGenerator() {
        this.generator = new UuidV7Generator();
    }

    @Override
    public UUID generateId(String primaryLabel, Object entity) {
        Assert.hasText(primaryLabel, "Primary label must not be empty");
        Assert.notNull(entity, "Entity must not be null");

        return this.generateId();
    }

    public UUID generateId() {
        return this.generator.generate();
    }
}
