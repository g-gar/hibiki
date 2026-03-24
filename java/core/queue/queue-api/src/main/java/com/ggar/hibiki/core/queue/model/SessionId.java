package com.ggar.hibiki.core.queue.model;

import java.util.UUID;

public record SessionId(UUID value) {
    public static SessionId of(UUID value) {
        return new SessionId(value);
    }
}
