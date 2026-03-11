package com.ggar.hibiki.core.catalog.persistence.mapper;

import com.ggar.hibiki.core.catalog.dto.AlbumDto;
import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.persistence.entity.AlbumEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {ArtistMapper.class},
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AlbumMapper {
    Album toDomain(AlbumEntity entity);

    AlbumEntity toEntity(Album domain);

    AlbumDto toDto(Album domain);

    AlbumDto toDto(AlbumEntity entity);
}
