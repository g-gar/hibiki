package com.ggar.hibiki.features.history.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Command to record a new playback event in the history.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class RecordPlaybackCommand implements Command<PlaybackHistoryEntry> {
    UUID userId;
    UUID sessionId;
    UUID deviceId;
    UUID songId;
    String contextType;
    UUID contextId;
}
