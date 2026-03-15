package com.ggar.hibiki.core.catalog.handler.query;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import java.util.UUID;

/**
 * Interface for the query handler that finds an Album in the catalog.
 */
public interface FindAlbumQueryHandler extends QueryHandler<FindAlbumQueryHandler.Find, Album> {

    /**
     * Query to find an album by ID, barcode, or title.
     */
    record Find(UUID id, String barcode, String title) implements Query<Album> {}
}
