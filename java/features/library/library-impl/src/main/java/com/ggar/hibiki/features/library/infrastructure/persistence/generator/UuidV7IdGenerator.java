package com.ggar.hibiki.features.library.infrastructure.persistence.generator;

import com.ggar.hibiki.packages.uuid.UuidV7Generator;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.IdGenerator;

/**
 * Custom ID generator using UUID v7 for Neo4j entities.
 */
public class UuidV7IdGenerator implements IdGenerator<UUID> {

    private final UuidV7Generator generator;

    public UuidV7IdGenerator() {
        this.generator = new UuidV7Generator();
    }

    @Override
    public UUID generateId(String primaryLabel, Object entity) {
        return generator.generate();
    }
}
