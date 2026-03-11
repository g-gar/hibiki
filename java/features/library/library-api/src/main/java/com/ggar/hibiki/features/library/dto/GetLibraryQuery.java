package com.ggar.hibiki.features.library.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.library.model.LibraryFilter;
import com.ggar.hibiki.features.library.model.Page;
import com.ggar.hibiki.features.library.model.Pagination;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Unified query to retrieve library items with filtering and pagination.
 */
@Value
@Builder(toBuilder = true)
@With
public class GetLibraryQuery implements Query<Page<LibraryItemDto>> {
    /**
     * User identifier.
     */
    UUID userId;
    /**
     * Filtering criteria.
     */
    LibraryFilter filter;
    /**
     * Pagination request.
     */
    Pagination pagination;
}
