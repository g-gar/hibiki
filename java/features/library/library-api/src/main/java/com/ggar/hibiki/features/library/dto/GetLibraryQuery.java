package com.ggar.hibiki.features.library.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.library.model.IdentityContext;
import com.ggar.hibiki.features.library.model.LibraryFilter;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.Page;
import com.ggar.hibiki.features.library.model.Pagination;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Unified query to retrieve library items with filtering and pagination.
 */
@Value
@Builder(toBuilder = true)
@With
public class GetLibraryQuery implements Query<Page<LibraryItem>> {
    /**
     * User identity context.
     */
    IdentityContext identityContext;
    /**
     * Filtering criteria.
     */
    LibraryFilter filter;
    /**
     * Pagination request.
     */
    Pagination pagination;
}
