package com.ggar.hibiki.core.catalog.persistence.mapper;

import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.catalog.persistence.entity.SongEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {ArtistMapper.class, AlbumMapper.class})
public interface SongMapper {
    Song toDomain(SongEntity entity);

    SongEntity toEntity(Song domain);
}
