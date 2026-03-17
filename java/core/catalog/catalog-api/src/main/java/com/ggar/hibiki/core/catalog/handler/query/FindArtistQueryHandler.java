package com.ggar.hibiki.core.catalog.handler.query;

import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import java.util.UUID;

/**
 * Interface for the query handler that finds an Artist in the catalog.
 */
public interface FindArtistQueryHandler extends QueryHandler<FindArtistQueryHandler.Find, Artist> {

    /**
     * Query to find an artist by ID, ISNI, or name.
     */
    record Find(UUID id, String isni, String name) implements Query<Artist> {}
}
