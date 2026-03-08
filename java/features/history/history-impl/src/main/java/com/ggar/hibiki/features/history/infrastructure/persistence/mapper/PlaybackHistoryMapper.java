package com.ggar.hibiki.features.history.infrastructure.persistence.mapper;

import com.ggar.hibiki.features.history.infrastructure.persistence.entity.PlaybackHistoryEntity;
import com.ggar.hibiki.features.history.model.ContextType;
import com.ggar.hibiki.features.history.model.IdentityContext;
import com.ggar.hibiki.features.history.model.PlaybackContext;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlaybackHistoryMapper {

    @Mapping(target = "userId", source = "identityContext.userId")
    @Mapping(target = "sessionId", source = "identityContext.sessionId")
    @Mapping(target = "deviceId", source = "identityContext.deviceId")
    @Mapping(target = "contextType", source = "playbackContext.type")
    @Mapping(target = "contextId", source = "playbackContext.id")
    PlaybackHistoryEntity toEntity(PlaybackHistoryEntry domain);

    @Mapping(target = "identityContext", source = "entity", qualifiedByName = "toIdentityContext")
    @Mapping(target = "playbackContext", source = "entity", qualifiedByName = "toPlaybackContext")
    PlaybackHistoryEntry toDomain(PlaybackHistoryEntity entity);

    @Named("toIdentityContext")
    default IdentityContext toIdentityContext(PlaybackHistoryEntity entity) {
        return IdentityContext.builder()
                .userId(entity.getUserId())
                .sessionId(entity.getSessionId())
                .deviceId(entity.getDeviceId())
                .build();
    }

    @Named("toPlaybackContext")
    default PlaybackContext toPlaybackContext(PlaybackHistoryEntity entity) {
        if (entity.getContextType() == null) return null;
        return PlaybackContext.builder()
                .type(ContextType.valueOf(entity.getContextType()))
                .id(entity.getContextId() != null ? UUID.fromString(entity.getContextId()) : null)
                .build();
    }
}
