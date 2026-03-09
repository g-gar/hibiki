package com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper;

import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.MediaEntity;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.UploadItemEntity;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.UploadSessionEntity;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.UserEntity;
import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import com.ggar.hibiki.features.ingestion.model.Media;
import com.ggar.hibiki.features.ingestion.model.MediaStatus;
import com.ggar.hibiki.features.ingestion.model.UploadItem;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MediaMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "mediaStatusToDomain")
    Media toDomain(MediaEntity entity);

    @Mapping(target = "status", source = "status", qualifiedByName = "mediaStatusToEntity")
    MediaEntity toEntity(Media domain);

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);

    @Mapping(target = "phase", source = "phase", qualifiedByName = "phaseToDomain")
    @Mapping(target = "userId", source = "user")
    UploadSession toDomain(UploadSessionEntity entity);

    @Mapping(target = "phase", source = "phase", qualifiedByName = "phaseToEntity")
    @Mapping(target = "user", source = "userId")
    UploadSessionEntity toEntity(UploadSession domain);

    @Mapping(target = "phase", source = "phase", qualifiedByName = "phaseToDomain")
    UploadItem toDomain(UploadItemEntity entity);

    @Mapping(target = "phase", source = "phase", qualifiedByName = "phaseToEntity")
    UploadItemEntity toEntity(UploadItem domain);

    @Named("mediaStatusToDomain")
    default MediaStatus mediaStatusToDomain(String status) {
        return status != null ? MediaStatus.valueOf(status) : null;
    }

    @Named("mediaStatusToEntity")
    default String mediaStatusToEntity(MediaStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("phaseToDomain")
    default IngestionPhase phaseToDomain(String phase) {
        return phase != null ? IngestionPhase.valueOf(phase) : null;
    }

    @Named("phaseToEntity")
    default String phaseToEntity(IngestionPhase phase) {
        return phase != null ? phase.name() : null;
    }
}
