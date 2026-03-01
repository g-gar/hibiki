package com.ggar.hibiki.core.identity.persistence.mapper;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
