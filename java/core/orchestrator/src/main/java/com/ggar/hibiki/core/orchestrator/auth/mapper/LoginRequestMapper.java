package com.ggar.hibiki.core.orchestrator.auth.mapper;

import com.ggar.hibiki.core.identity.dto.LoginRequest;
import com.ggar.hibiki.core.orchestrator.auth.model.LoginRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface LoginRequestMapper {

    @Mapping(target = "username", source = "dto.username")
    @Mapping(target = "password", source = "dto.password")
    @Mapping(target = "ip", source = "ip")
    @Mapping(target = "userAgent", source = "userAgent")
    @Mapping(target = "deviceId", ignore = true)
    LoginRequest toCommand(LoginRequestDTO dto, String ip, String userAgent);
}
