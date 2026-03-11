package com.ggar.hibiki.features.history.infrastructure.persistence.entity;

import com.ggar.hibiki.features.history.infrastructure.persistence.generator.UuidV7IdGenerator;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("PlaybackHistory")
public class PlaybackHistoryEntity {

    @Id
    @GeneratedValue(UuidV7IdGenerator.class)
    private UUID id;

    private UUID userId;
    private UUID sessionId;
    private UUID deviceId;
    private UUID songId;
    private String contextType;
    private UUID contextId;
    private Instant playedAt;

    public PlaybackHistoryEntity() {}

    public PlaybackHistoryEntity(
            UUID id,
            UUID userId,
            UUID sessionId,
            UUID deviceId,
            UUID songId,
            String contextType,
            UUID contextId,
            Instant playedAt) {
        this.id = id;
        this.userId = userId;
        this.sessionId = sessionId;
        this.deviceId = deviceId;
        this.songId = songId;
        this.contextType = contextType;
        this.contextId = contextId;
        this.playedAt = playedAt;
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

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public UUID getSongId() {
        return songId;
    }

    public void setSongId(UUID songId) {
        this.songId = songId;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public UUID getContextId() {
        return contextId;
    }

    public void setContextId(UUID contextId) {
        this.contextId = contextId;
    }

    public Instant getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(Instant playedAt) {
        this.playedAt = playedAt;
    }
}
