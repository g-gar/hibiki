package com.ggar.hibiki.core.catalog.handler.query;

import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import java.util.UUID;

/**
 * Interface for the query handler that finds a Song in the catalog.
 */
public interface FindSongQueryHandler extends QueryHandler<FindSongQueryHandler.Find, Song> {

    /**
     * Query to find a song by ID.
     */
    record Find(UUID id) implements Query<Song> {}
}
