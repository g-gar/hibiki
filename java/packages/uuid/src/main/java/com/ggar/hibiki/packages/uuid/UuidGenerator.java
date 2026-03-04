package com.ggar.hibiki.packages.uuid;

import java.util.UUID;

/**
 * Interface for generating specific versions of UUIDs.
 */
public interface UuidGenerator {

    /**
     * Generates a new UUID.
     *
     * @return A newly generated UUID.
     */
    UUID generate();
}
