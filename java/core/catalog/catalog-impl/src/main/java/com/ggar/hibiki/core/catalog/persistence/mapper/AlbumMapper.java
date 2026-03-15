package com.ggar.hibiki.core.catalog.persistence.mapper;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.persistence.entity.AlbumEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {ArtistMapper.class})
public interface AlbumMapper {
    Album toDomain(AlbumEntity entity);

    AlbumEntity toEntity(Album domain);
}
