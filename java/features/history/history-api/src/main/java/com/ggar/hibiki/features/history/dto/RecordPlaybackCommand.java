package com.ggar.hibiki.features.history.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.history.model.IdentityContext;
import com.ggar.hibiki.features.history.model.PlaybackContext;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to record a new playback event in the history.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordPlaybackCommand implements Command<PlaybackHistoryEntry> {
    private IdentityContext identityContext;
    private UUID songId;
    private PlaybackContext playbackContext;
}
