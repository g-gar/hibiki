package com.ggar.hibiki.features.devices.infrastructure.persistence.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.Instant;

@Node("Device")
public class DeviceEntity {

    @Id
    private String id;

    @Property("userId")
    private String userId;

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

    public DeviceEntity() {
    }

    public DeviceEntity(String id, String userId, String name, String type, String status, String userAgent,
            String lastIp, Instant createdAt, Instant lastSeenAt) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
