package com.ggar.hibiki.test.fixtures;

import com.ggar.hibiki.core.catalog.dto.AlbumDto;
import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.core.catalog.dto.SongDto;
import java.util.UUID;

public final class CatalogFixtures {

    private CatalogFixtures() {}

    public static ArtistDto artistDto(UUID id, String name) {
        return ArtistDto.builder().id(id.toString()).name(name).build();
    }

    public static AlbumDto albumDto(UUID id, String title, Integer year, String artistName) {
        return AlbumDto.builder()
                .id(id.toString())
                .title(title)
                .releaseYear(year)
                .artist(ArtistDto.builder().name(artistName).build())
                .build();
    }

    public static SongDto songDto(UUID id, String title, UUID albumId) {
        return SongDto.builder()
                .id(id.toString())
                .title(title)
                .album(AlbumDto.builder().id(albumId.toString()).build())
                .build();
    }
}
