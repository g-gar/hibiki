package com.ggar.hibiki.presentation.restapi.mapper;

import com.ggar.hibiki.core.identity.dto.SignupRequest;
import com.ggar.hibiki.core.orchestrator.auth.model.SignupRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthMapper {
    SignupRequest toDomain(SignupRequestDTO dto);
}
