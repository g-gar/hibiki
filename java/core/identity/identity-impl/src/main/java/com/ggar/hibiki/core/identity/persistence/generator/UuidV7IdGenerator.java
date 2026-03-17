package com.ggar.hibiki.core.identity.persistence.generator;

import com.ggar.hibiki.packages.uuid.UuidV7Generator;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.IdGenerator;

public class UuidV7IdGenerator implements IdGenerator<UUID> {

    @Override
    public UUID generateId(String primaryLabel, Object entity) {
        return UuidV7Generator.generate();
    }
}
