package com.ggar.hibiki.features.devices.infrastructure.persistence.entity;

import com.ggar.hibiki.features.devices.infrastructure.persistence.generator.UuidV7IdGenerator;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Device")
public class DeviceEntity {

    @Id
    @GeneratedValue(UuidV7IdGenerator.class)
    private UUID id;

    @Property("userId")
    private UUID userId;

    @Property("name")
    private String name;

    @Property("type")
    private String type;

    @Property("status")
    private String status;

    @Property("userAgent")
    private String userAgent;

    @Property("lastIp")
    private String lastIp;

    @Property("createdAt")
    private Instant createdAt;

    @Property("lastSeenAt")
    private Instant lastSeenAt;

    public DeviceEntity() {}

    public DeviceEntity(
            UUID id,
            UUID userId,
            String name,
            String type,
            String status,
            String userAgent,
            String lastIp,
            Instant createdAt,
            Instant lastSeenAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.userAgent = userAgent;
        this.lastIp = lastIp;
        this.createdAt = createdAt;
        this.lastSeenAt = lastSeenAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(Instant lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }
}
