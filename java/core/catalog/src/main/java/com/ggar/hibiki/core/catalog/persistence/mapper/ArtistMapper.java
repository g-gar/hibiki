package com.ggar.hibiki.core.catalog.persistence.mapper;

import com.ggar.hibiki.core.catalog.model.domain.Artist;
import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    Artist toDomain(ArtistEntity entity);

    ArtistEntity toEntity(Artist domain);
}
