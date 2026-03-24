package com.ggar.hibiki.core.queue.model;

import java.util.UUID;

public record QueueId(UUID value) {
    public static QueueId next() {
        return new QueueId(UUID.randomUUID());
    }

    public static QueueId of(UUID value) {
        return new QueueId(value);
    }
}
