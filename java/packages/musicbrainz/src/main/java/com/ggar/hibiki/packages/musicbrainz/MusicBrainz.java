package com.ggar.hibiki.packages.musicbrainz;

import com.ggar.hibiki.packages.musicbrainz.model.EntityType;
import reactor.core.publisher.Mono;

/**
 * The main interface for interacting with the MusicBrainz API.
 * Provides generic flexible query methods.
 */
public interface MusicBrainz {

    /**
     * Looks up an entity using its unique MusicBrainz Identifier (MBID).
     *
     * @param entity       the type of entity to look up
     * @param mbid         the unique MusicBrainz identifier for the entity
     * @param responseType the class describing the shape of the expected response
     * @param includeMask  a bitmask of
     *                     {@link com.ggar.hibiki.packages.musicbrainz.model.Include}
     *                     flags
     *                     (e.g., Include.RECORDINGS.getMask() |
     *                     Include.ISRCS.getMask())
     * @param <T>          the type of the response
     * @return a {@link Mono} emitting the deserialized entity
     */
    <T> Mono<T> lookup(EntityType entity, String mbid, Class<T> responseType, long includeMask);

    /**
     * Performs a non-MBID lookup based on an ISRC (International Standard Recording
     * Code).
     * Note that this may return multiple matched entities.
     *
     * @param isrc         the ISRC to look up
     * @param responseType the class describing the shape of the expected response
     * @param includeMask  a bitmask of
     *                     {@link com.ggar.hibiki.packages.musicbrainz.model.Include}
     *                     flags
     * @param <T>          the type of the response
     * @return a {@link Mono} emitting the deserialized response
     */
    <T> Mono<T> lookupByIsrc(String isrc, Class<T> responseType, long includeMask);

    /**
     * Performs a non-MBID lookup based on a Disc ID.
     * Note that this may return multiple matched entities.
     *
     * @param discId       the Disc ID to look up
     * @param responseType the class describing the shape of the expected response
     * @param includeMask  a bitmask of
     *                     {@link com.ggar.hibiki.packages.musicbrainz.model.Include}
     *                     flags
     * @param <T>          the type of the response
     * @return a {@link Mono} emitting the deserialized response
     */
    <T> Mono<T> lookupByDiscid(String discId, Class<T> responseType, long includeMask);
}
