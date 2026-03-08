package com.ggar.hibiki.features.library.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Pagination request details.
 */
@Value
@Builder(toBuilder = true)
@With
public class Pagination {
    /**
     * Page number (starting from 0).
     */
    int page;
    /**
     * Number of items per page.
     */
    int size;

    /**
     * Default pagination (first page, 50 items).
     * @return a default pagination instance
     */
    public static Pagination defaultPagination() {
        return Pagination.builder().page(0).size(50).build();
    }
}
