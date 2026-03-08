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
    private String sessionId;
    private UUID deviceId;
    private String songId;
    private String contextType;
    private String contextId;
    private Instant playedAt;

    public PlaybackHistoryEntity() {}

    public PlaybackHistoryEntity(
            UUID id,
            UUID userId,
            String sessionId,
            UUID deviceId,
            String songId,
            String contextType,
            String contextId,
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public Instant getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(Instant playedAt) {
        this.playedAt = playedAt;
    }
}
