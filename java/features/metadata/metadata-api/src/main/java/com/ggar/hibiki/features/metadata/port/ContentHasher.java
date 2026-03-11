package com.ggar.hibiki.features.metadata.port;

/**
 * Port for content hashing, abstracting the algorithm used.
 * Supports progressive (incremental) hashing across chunks.
 * The implementation determines the concrete algorithm (SHA-256, etc.).
 */
public interface ContentHasher {

    /**
     * Opaque state handle for an in-progress hash computation.
     */
    interface HashState {}

    /**
     * Initializes a new hash computation.
     */
    HashState init();

    /**
     * Feeds data into the hash computation incrementally.
     */
    HashState update(HashState state, byte[] data);

    /**
     * Finalizes the hash and returns the hex-encoded digest.
     */
    String finalize(HashState state);
}
