package com.ggar.hibiki.test.fixtures;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.model.Song;
import java.util.UUID;

public final class CatalogFixtures {

    private CatalogFixtures() {}

    public static Artist artist(UUID id, String name) {
        return Artist.builder().id(id).name(name).build();
    }

    public static Album album(UUID id, String title, Integer year, String artistName) {
        return Album.builder()
                .id(id)
                .title(title)
                .releaseYear(year)
                .artist(Artist.builder().name(artistName).build())
                .build();
    }

    public static Song song(UUID id, String title, UUID albumId) {
        return Song.builder()
                .id(id)
                .title(title)
                .album(Album.builder().id(albumId).build())
                .build();
    }
}
