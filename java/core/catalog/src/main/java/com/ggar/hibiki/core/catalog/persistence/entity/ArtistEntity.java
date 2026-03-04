package com.ggar.hibiki.core.catalog.persistence.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import com.ggar.hibiki.core.catalog.persistence.generator.UuidV7IdGenerator;

import java.util.Objects;
import java.util.UUID;

@Node("Artist")
public class ArtistEntity {

    @Id
    @GeneratedValue(UuidV7IdGenerator.class)
    private String id;
    private String name;
    private String isni;

    public ArtistEntity() {
    }

    public ArtistEntity(String name) {
        this();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsni() {
        return isni;
    }

    public void setIsni(String isni) {
        this.isni = isni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ArtistEntity that = (ArtistEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
