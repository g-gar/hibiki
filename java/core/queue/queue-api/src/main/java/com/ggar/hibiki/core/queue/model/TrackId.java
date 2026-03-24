package com.ggar.hibiki.core.queue.model;

import java.util.UUID;

public record TrackId(UUID value) {
    public static TrackId of(UUID value) {
        return new TrackId(value);
    }
}
