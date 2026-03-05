package com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper;

import com.ggar.hibiki.features.ingestion.domain.Media;
import com.ggar.hibiki.features.ingestion.domain.User;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.MediaEntity;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    Media toDomain(MediaEntity entity);

    MediaEntity toEntity(Media domain);

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
