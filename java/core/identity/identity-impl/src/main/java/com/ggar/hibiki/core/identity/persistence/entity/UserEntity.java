package com.ggar.hibiki.core.identity.persistence.entity;

import com.ggar.hibiki.core.identity.persistence.generator.UuidV7IdGenerator;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("User")
public class UserEntity {

    @Id
    @GeneratedValue(UuidV7IdGenerator.class)
    private UUID id;

    @Property("username")
    private String username;

    @Property("email")
    private String email;

    @Property("password")
    private String password;

    @Property("roles")
    private Set<String> roles;

    @Property("twoFactorEnabled")
    private boolean twoFactorEnabled;

    public UserEntity() {}

    public UserEntity(
            UUID id, String username, String email, String password, Set<String> roles, boolean twoFactorEnabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }
}
