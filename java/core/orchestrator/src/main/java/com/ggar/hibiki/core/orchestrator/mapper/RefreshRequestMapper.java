package com.ggar.hibiki.core.orchestrator.mapper;

import com.ggar.hibiki.core.identity.dto.RefreshAuthRequest;
import com.ggar.hibiki.core.orchestrator.dto.RefreshRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RefreshRequestMapper {

    RefreshAuthRequest toCommand(RefreshRequestDTO dto);
}
