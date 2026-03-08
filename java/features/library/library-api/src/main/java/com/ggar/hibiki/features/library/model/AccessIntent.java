package com.ggar.hibiki.features.library.model;

import java.util.Map;
import lombok.Builder;
import lombok.Value;

/**
 * Represents an abstract intention to access specific library items.
 * This allows modules to contribute access rules without knowing the underlying persistence.
 */
@Value
@Builder
public class AccessIntent {

    public enum Type {
        /**
         * Adds nodes/paths to the result set (OR logic).
         * Example: "Owned items", "Shared items".
         */
        EXPANSION,
        /**
         * Filters the resulting set (AND logic).
         * Example: "Subscription tier restriction", "Geo-blocking".
         */
        RESTRICTION
    }

    /**
     * Unique identifier for the intent (e.g., "SHARED_WITH_ME").
     */
    String id;
    /**
     * The type of access logic to apply.
     */
    Type type;
    /**
     * Contextual parameters for the access rule.
     */
    Map<String, Object> parameters;

    public static final String SHARED_WITH_ME = "SHARED_WITH_ME";
    public static final String ENTITLEMENT_FILTER = "ENTITLEMENT_FILTER";
}
