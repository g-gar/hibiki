package com.ggar.hibiki.core.catalog.persistence.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import com.ggar.hibiki.core.catalog.persistence.generator.UuidV7IdGenerator;

import java.util.Objects;

@Node("Album")
public class AlbumEntity {

    @Id
    @GeneratedValue(UuidV7IdGenerator.class)
    private String id;
    private String title;
    private Integer releaseYear;
    private String barcode;

    @Relationship(type = "RELEASED_BY", direction = Relationship.Direction.OUTGOING)
    private ArtistEntity artist;

    public AlbumEntity() {
    }

    public AlbumEntity(String title, Integer releaseYear) {
        this();
        this.title = title;
        this.releaseYear = releaseYear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public ArtistEntity getArtist() {
        return artist;
    }

    public void setArtist(ArtistEntity artist) {
        this.artist = artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AlbumEntity that = (AlbumEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
