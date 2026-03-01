package com.ggar.hibiki.presentation.restapi.mapper;

import com.ggar.hibiki.core.identity.model.LoginRequest;
import com.ggar.hibiki.core.identity.model.SignupRequest;
import com.ggar.hibiki.presentation.restapi.model.LoginRequestDTO;
import com.ggar.hibiki.presentation.restapi.model.SignupRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    SignupRequest toDomain(SignupRequestDTO dto);

    LoginRequest toDomain(LoginRequestDTO dto);
}
