package com.ggar.hibiki.core.queue.infrastructure.shuffle;

import com.ggar.hibiki.core.queue.model.TrackId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Engine responsible for shuffling a list of tracks while maintaining certain constraints.
 */
@Component
public class ShuffleEngine {

    /**
     * Shuffles the given list of tracks.
     *
     * @param tracks The list of tracks to shuffle.
     * @param currentTrackIndex The index of the currently playing track,
     * which should stay at the top or be handled specially.
     * @return A new list of tracks shuffled.
     */
    public List<TrackId> shuffle(List<TrackId> tracks, int currentTrackIndex) {
        if (tracks == null || tracks.isEmpty()) {
            return new ArrayList<>();
        }

        List<TrackId> shuffled = new ArrayList<>(tracks);

        // If there's a current track, we usually keep it at the current position or move it to 0
        // and shuffle the rest. For simplicity, we'll keep the current track at its index
        // and shuffle everything else, or just shuffle everything and then find where the current track ended up.
        // A common requirement is that shuffle doesn't change the "now playing" track.

        TrackId currentTrack = null;
        if (currentTrackIndex >= 0 && currentTrackIndex < tracks.size()) {
            currentTrack = tracks.get(currentTrackIndex);
            shuffled.remove(currentTrackIndex);
        }

        Collections.shuffle(shuffled);

        if (currentTrack != null) {
            shuffled.add(0, currentTrack);
        }

        return shuffled;
    }
}
