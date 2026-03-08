package com.ggar.hibiki.core.orchestrator.auth.mapper;

import com.ggar.hibiki.core.identity.dto.RefreshAuthRequest;
import com.ggar.hibiki.core.orchestrator.auth.model.RefreshRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RefreshRequestMapper {

    RefreshAuthRequest toCommand(RefreshRequestDTO dto);
}
