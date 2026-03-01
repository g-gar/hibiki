package com.ggar.hibiki.core.catalog.persistence.mapper;

import com.ggar.hibiki.core.catalog.model.domain.Album;
import com.ggar.hibiki.core.catalog.persistence.entity.AlbumEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ArtistMapper.class })
public interface AlbumMapper {
    Album toDomain(AlbumEntity entity);

    AlbumEntity toEntity(Album domain);
}
