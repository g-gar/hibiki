package com.ggar.hibiki.core.identity.persistence.generator;

import com.ggar.hibiki.packages.uuid.UuidV7Generator;
import org.springframework.data.neo4j.core.schema.IdGenerator;

public class UuidV7IdGenerator implements IdGenerator<String> {

    private final UuidV7Generator generator;

    public UuidV7IdGenerator() {
        this.generator = new UuidV7Generator();
    }

    @Override
    public String generateId(String primaryLabel, Object entity) {
        return generator.generate().toString();
    }
}
