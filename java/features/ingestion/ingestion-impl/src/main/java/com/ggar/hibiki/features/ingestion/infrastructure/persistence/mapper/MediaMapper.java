package com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper;

import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.MediaEntity;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.UserEntity;
import com.ggar.hibiki.features.ingestion.model.Media;
import com.ggar.hibiki.features.ingestion.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MediaMapper {

    Media toDomain(MediaEntity entity);

    MediaEntity toEntity(Media domain);

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
