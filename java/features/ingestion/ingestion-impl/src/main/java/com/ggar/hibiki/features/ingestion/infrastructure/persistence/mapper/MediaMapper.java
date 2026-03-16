package com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper;

import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.MediaEntity;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.UploadItemEntity;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.UploadSessionEntity;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.UserEntity;
import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import com.ggar.hibiki.features.ingestion.model.Media;
import com.ggar.hibiki.features.ingestion.model.MediaId;
import com.ggar.hibiki.features.ingestion.model.MediaStatus;
import com.ggar.hibiki.features.ingestion.model.UploadItem;
import com.ggar.hibiki.features.ingestion.model.UploadItemId;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.model.User;
import com.ggar.hibiki.features.ingestion.model.UserId;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MediaMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToMediaId")
    @Mapping(target = "status", source = "status", qualifiedByName = "mediaStatusToDomain")
    Media toDomain(MediaEntity entity);

    @Mapping(target = "id", source = "id", qualifiedByName = "mediaIdToUuid")
    @Mapping(target = "status", source = "status", qualifiedByName = "mediaStatusToEntity")
    MediaEntity toEntity(Media domain);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToUserId")
    User toDomain(UserEntity entity);

    @Mapping(target = "id", source = "id", qualifiedByName = "userIdToUuid")
    UserEntity toEntity(User domain);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToUploadSessionId")
    @Mapping(target = "phase", source = "phase", qualifiedByName = "phaseToDomain")
    @Mapping(target = "userId", source = "user")
    UploadSession toDomain(UploadSessionEntity entity);

    @Mapping(target = "id", source = "id", qualifiedByName = "uploadSessionIdToUuid")
    @Mapping(target = "phase", source = "phase", qualifiedByName = "phaseToEntity")
    @Mapping(target = "user", source = "userId")
    UploadSessionEntity toEntity(UploadSession domain);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToUploadItemId")
    @Mapping(target = "phase", source = "phase", qualifiedByName = "phaseToDomain")
    @Mapping(target = "mediaId", source = "media", qualifiedByName = "mediaEntityToMediaId")
    UploadItem toDomain(UploadItemEntity entity);

    @Mapping(target = "id", source = "id", qualifiedByName = "uploadItemIdToUuid")
    @Mapping(target = "phase", source = "phase", qualifiedByName = "phaseToEntity")
    @Mapping(target = "media", source = "mediaId", qualifiedByName = "mediaIdToMediaEntity")
    UploadItemEntity toEntity(UploadItem domain);

    @Named("mapMediaIdToString")
    default String mapMediaIdToString(MediaId value) {
        return value != null ? value.getId().toString() : null;
    }

    @Named("mapUserToUserIdString")
    default String mapUserToUserIdString(User value) {
        return (value != null && value.getId() != null) ? value.getId().getId().toString() : null;
    }

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

    @Named("mediaEntityToMediaId")
    default MediaId mediaEntityToMediaId(MediaEntity entity) {
        return entity != null ? MediaId.of(entity.getId()) : null;
    }

    @Named("mediaIdToMediaEntity")
    default MediaEntity mediaIdToMediaEntity(MediaId mediaId) {
        return mediaId != null ? MediaEntity.builder().id(mediaId.getId()).build() : null;
    }

    @Named("uuidToMediaId")
    default MediaId uuidToMediaId(UUID id) {
        return id != null ? MediaId.of(id) : null;
    }

    @Named("mediaIdToUuid")
    default UUID mediaIdToUuid(MediaId id) {
        return id != null ? id.getId() : null;
    }

    @Named("uuidToUserId")
    default UserId uuidToUserId(UUID id) {
        return id != null ? UserId.of(id) : null;
    }

    @Named("userIdToUuid")
    default UUID userIdToUuid(UserId id) {
        return id != null ? id.getId() : null;
    }

    @Named("uuidToUploadSessionId")
    default UploadSessionId uuidToUploadSessionId(UUID id) {
        return id != null ? UploadSessionId.of(id) : null;
    }

    @Named("uploadSessionIdToUuid")
    default UUID uploadSessionIdToUuid(UploadSessionId id) {
        return id != null ? id.getId() : null;
    }

    @Named("uuidToUploadItemId")
    default UploadItemId uuidToUploadItemId(UUID id) {
        return id != null ? UploadItemId.of(id) : null;
    }

    @Named("uploadItemIdToUuid")
    default UUID uploadItemIdToUuid(UploadItemId id) {
        return id != null ? id.getId() : null;
    }
}
