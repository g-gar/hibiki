package com.ggar.hibiki.features.library.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * A paged collection of items.
 *
 * @param <T> The type of items in the page.
 */
@Value
@Builder(toBuilder = true)
@With
public class Page<T> {
    /**
     * Items in the current page.
     */
    List<T> items;
    /**
     * Cursor to retrieve the next page of results.
     * Null if there are no more pages.
     */
    String nextCursor;
    /**
     * Total number of items across all pages (optional, may be estimated).
     */
    long totalItems;
}
