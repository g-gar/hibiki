package com.ggar.hibiki.core.catalog.persistence.mapper;

import com.ggar.hibiki.core.catalog.model.domain.Song;
import com.ggar.hibiki.core.catalog.persistence.entity.SongEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ArtistMapper.class, AlbumMapper.class })
public interface SongMapper {
    Song toDomain(SongEntity entity);

    SongEntity toEntity(Song domain);
}
