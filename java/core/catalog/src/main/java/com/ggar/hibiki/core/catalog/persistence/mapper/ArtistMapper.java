package com.ggar.hibiki.core.catalog.persistence.mapper;

import com.ggar.hibiki.core.catalog.model.domain.Artist;
import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ArtistMapper {
    Artist toDomain(ArtistEntity entity);

    ArtistEntity toEntity(Artist domain);
}
