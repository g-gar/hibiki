package com.ggar.hibiki.core.queue.infrastructure.persistence.mapper;

import com.ggar.hibiki.core.queue.infrastructure.persistence.entity.PlaybackQueueEntity;
import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.QueueId;
import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.model.TrackId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between PlaybackQueue domain models and PlaybackQueueEntity persistence entities.
 */
@Mapper(componentModel = "spring")
public interface PlaybackQueueMapper {

    /**
     * Converts a persistence entity to a domain model.
     *
     * @param entity The persistence entity.
     * @return The domain model.
     */
    @Mapping(target = "queueId", source = "queueId")
    @Mapping(target = "sessionId", source = "sessionId")
    PlaybackQueue toDomain(PlaybackQueueEntity entity);

    /**
     * Converts a domain model to a persistence entity.
     *
     * @param domain The domain model.
     * @return The persistence entity.
     */
    @Mapping(target = "queueId", source = "queueId")
    @Mapping(target = "sessionId", source = "sessionId")
    PlaybackQueueEntity toEntity(PlaybackQueue domain);

    /**
     * Maps UUID to QueueId.
     *
     * @param value The UUID value.
     * @return The QueueId.
     */
    default QueueId mapToQueueId(UUID value) {
        return value != null ? QueueId.of(value) : null;
    }

    /**
     * Maps QueueId to UUID.
     *
     * @param id The QueueId.
     * @return The UUID value.
     */
    default UUID mapFromQueueId(QueueId id) {
        return id != null ? id.value() : null;
    }

    /**
     * Maps UUID to SessionId.
     *
     * @param value The UUID value.
     * @return The SessionId.
     */
    default SessionId mapToSessionId(UUID value) {
        return value != null ? SessionId.of(value) : null;
    }

    /**
     * Maps SessionId to UUID.
     *
     * @param id The SessionId.
     * @return The UUID value.
     */
    default UUID mapFromSessionId(SessionId id) {
        return id != null ? id.value() : null;
    }

    /**
     * Maps list of UUIDs to list of TrackIds.
     *
     * @param values The list of UUIDs.
     * @return The list of TrackIds.
     */
    default List<TrackId> mapToTrackIds(List<UUID> values) {
        if (values == null) {
            return null;
        }
        return values.stream().map(TrackId::of).collect(Collectors.toList());
    }

    /**
     * Maps list of TrackIds to list of UUIDs.
     *
     * @param ids The list of TrackIds.
     * @return The list of UUIDs.
     */
    default List<UUID> mapFromTrackIds(List<TrackId> ids) {
        if (ids == null) {
            return null;
        }
        return ids.stream().map(TrackId::value).collect(Collectors.toList());
    }
}
