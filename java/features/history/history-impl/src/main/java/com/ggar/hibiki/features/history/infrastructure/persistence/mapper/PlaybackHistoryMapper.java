package com.ggar.hibiki.features.history.infrastructure.persistence.mapper;

import com.ggar.hibiki.features.history.infrastructure.persistence.entity.PlaybackHistoryEntity;
import com.ggar.hibiki.features.history.model.ContextType;
import com.ggar.hibiki.features.history.model.DeviceId;
import com.ggar.hibiki.features.history.model.IdentityContext;
import com.ggar.hibiki.features.history.model.PlaybackContext;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import com.ggar.hibiki.features.history.model.PlaybackHistoryId;
import com.ggar.hibiki.features.history.model.SessionId;
import com.ggar.hibiki.features.history.model.SongId;
import com.ggar.hibiki.features.history.model.UserId;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlaybackHistoryMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "userId", source = "identityContext.userId.value")
    @Mapping(target = "sessionId", source = "identityContext.sessionId.value")
    @Mapping(target = "deviceId", source = "identityContext.deviceId.value")
    @Mapping(target = "songId", source = "songId.value")
    @Mapping(target = "contextType", source = "playbackContext.type")
    @Mapping(target = "contextId", source = "playbackContext.id")
    PlaybackHistoryEntity toEntity(PlaybackHistoryEntry domain);

    @Mapping(target = "id", source = "id", qualifiedByName = "toPlaybackHistoryId")
    @Mapping(target = "songId", source = "songId", qualifiedByName = "toSongId")
    @Mapping(target = "identityContext", source = "entity", qualifiedByName = "toIdentityContext")
    @Mapping(target = "playbackContext", source = "entity", qualifiedByName = "toPlaybackContext")
    PlaybackHistoryEntry toDomain(PlaybackHistoryEntity entity);

    @Named("toIdentityContext")
    default IdentityContext toIdentityContext(PlaybackHistoryEntity entity) {
        return IdentityContext.builder()
                .userId(UserId.of(entity.getUserId()))
                .sessionId(entity.getSessionId() != null ? SessionId.of(entity.getSessionId()) : null)
                .deviceId(DeviceId.of(entity.getDeviceId()))
                .build();
    }

    @Named("toPlaybackContext")
    default PlaybackContext toPlaybackContext(PlaybackHistoryEntity entity) {
        if (entity.getContextType() == null) return null;
        return PlaybackContext.builder()
                .type(ContextType.valueOf(entity.getContextType()))
                .id(entity.getContextId())
                .build();
    }

    @Named("toPlaybackHistoryId")
    default PlaybackHistoryId toPlaybackHistoryId(UUID id) {
        return id != null ? PlaybackHistoryId.of(id) : null;
    }

    @Named("toSongId")
    default SongId toSongId(UUID songId) {
        return songId != null ? SongId.of(songId) : null;
    }
}
