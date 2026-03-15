package com.ggar.hibiki.core.orchestrator.mapper;

import com.ggar.hibiki.core.identity.handler.command.RefreshAuthCommandHandler;
import com.ggar.hibiki.core.orchestrator.dto.RefreshRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RefreshRequestMapper {

    RefreshAuthCommandHandler.Refresh toCommand(RefreshRequestDTO dto);
}
