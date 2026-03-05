package com.ggar.hibiki.packages.jwt.signer;

import java.util.Map;
import reactor.core.publisher.Mono;

/**
 * Interface defining the contract for generating a signed JWT.
 */
public interface JwtSigner {

    /**
     * Generates a signed JWT for the given subject.
     *
     * @param subject the subject of the token (e.g., userId)
     * @return a Mono emitting the signed JWT string
     */
    Mono<String> generateToken(String subject);

    /**
     * Generates a signed JWT for the given subject with additional claims.
     *
     * @param subject the subject of the token (e.g., userId)
     * @param claims  additional claims to include in the payload
     * @return a Mono emitting the signed JWT string
     */
    Mono<String> generateToken(String subject, Map<String, Object> claims);

    /**
     * Generates a signed JWT for the given subject with a custom expiration time.
     *
     * @param subject        the subject of the token (e.g., userId)
     * @param expirationTime custom expiration time in milliseconds
     * @return a Mono emitting the signed JWT string
     */
    Mono<String> generateToken(String subject, long expirationTime);

    /**
     * Generates a signed JWT for the given subject with additional claims and a
     * custom expiration time.
     *
     * @param subject        the subject of the token (e.g., userId)
     * @param claims         additional claims to include in the payload
     * @param expirationTime custom expiration time in milliseconds
     * @return a Mono emitting the signed JWT string
     */
    Mono<String> generateToken(String subject, Map<String, Object> claims, long expirationTime);
}
