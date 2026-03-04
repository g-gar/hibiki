package com.ggar.hibiki.core.identity.persistence.mapper;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
