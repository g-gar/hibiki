package com.ggar.hibiki.features.library.model;

/**
 * Types of operations that can be performed on a playlist in a batch update.
 */
public enum PlaylistOperationType {
    /**
     * Add one or more songs to the playlist.
     */
    ADD_SONGS,
    /**
     * Remove one or more songs from the playlist.
     */
    REMOVE_SONGS,
    /**
     * Change the position of a song within the playlist.
     */
    MOVE_SONG,
    /**
     * Update the playlist metadata (name, description, etc.).
     */
    UPDATE_METADATA,
    /**
     * Update the playlist visibility.
     */
    UPDATE_VISIBILITY
}
