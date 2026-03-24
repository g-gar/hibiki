package com.ggar.hibiki.core.queue.infrastructure.persistence.entity;

import com.ggar.hibiki.core.queue.model.RepeatMode;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "playback_queue", timeToLive = 86400) // 1 día de TTL por defecto
public class PlaybackQueueEntity {
    @Id
    private UUID queueId;

    @Indexed
    private UUID sessionId;

    private List<UUID> tracks;

    private int currentIndex;

    private RepeatMode repeatMode;

    private boolean shuffled;
}
