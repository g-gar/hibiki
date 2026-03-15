package com.ggar.hibiki.core.identity.persistence.entity;

import com.ggar.hibiki.core.identity.persistence.generator.UuidV7IdGenerator;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("AuthContext")
public class AuthContextEntity {

    @Id
    @GeneratedValue(UuidV7IdGenerator.class)
    private UUID id;

    private String accessToken;

    private String refreshToken;

    public AuthContextEntity() {}

    public AuthContextEntity(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
