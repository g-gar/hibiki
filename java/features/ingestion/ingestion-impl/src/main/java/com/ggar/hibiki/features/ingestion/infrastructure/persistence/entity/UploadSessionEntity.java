package com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("UploadSession")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UploadSessionEntity {

    @Id
    private UUID id;

    @Relationship(type = "OWNS", direction = Relationship.Direction.INCOMING)
    private UserEntity user;

    @Property("phase")
    private String phase;

    @Property("createdAt")
    private Instant createdAt;

    @Property("completedAt")
    private Instant completedAt;

    @Relationship(type = "HAS_ITEM", direction = Relationship.Direction.OUTGOING)
    private List<UploadItemEntity> items;
}
