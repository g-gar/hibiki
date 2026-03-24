package com.ggar.hibiki.core.queue.model;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@Builder(toBuilder = true)
public class PlaybackQueue {
    /** Unique identifier for the queue. */
    QueueId queueId;
    /** Playback session this queue belongs to. */
    SessionId sessionId;
    /**
     * Ordered list of pending tracks. This list is the source of truth
     * for playback; shuffle reorders this list in memory.
     */
    List<TrackId> tracks;
    /** Index of the currently active track within {@code tracks}. */
    int currentIndex;
    /** Active repeat policy. */
    RepeatMode repeatMode;
    /** If true, the order of {@code tracks} has been randomized. */
    boolean shuffled;
}
