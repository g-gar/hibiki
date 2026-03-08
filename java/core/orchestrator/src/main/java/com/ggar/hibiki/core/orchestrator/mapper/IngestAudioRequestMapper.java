package com.ggar.hibiki.core.orchestrator.mapper;

import com.ggar.hibiki.core.orchestrator.dto.IngestAudioRequestDTO;
import com.ggar.hibiki.features.ingestion.dto.UploadAudioStreamCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IngestAudioRequestMapper {

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "contentStream", source = "dto.inputStream")
    UploadAudioStreamCommand toCommand(IngestAudioRequestDTO dto, String userId);
}
