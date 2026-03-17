package com.ggar.hibiki.core.orchestrator.mapper;

import com.ggar.hibiki.core.identity.handler.command.LoginCommandHandler;
import com.ggar.hibiki.core.orchestrator.dto.LoginRequestDTO;
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
    LoginCommandHandler.Login toCommand(LoginRequestDTO dto, String ip, String userAgent);
}
