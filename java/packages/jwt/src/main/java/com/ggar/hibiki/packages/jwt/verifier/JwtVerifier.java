package com.ggar.hibiki.packages.jwt.verifier;

import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Interface defining the contract for verifying a JWT and extracting its
 * claims.
 */
public interface JwtVerifier {

    /**
     * Verifies the given JWT token and extracts its claims.
     *
     * @param token the JWT string
     * @return a Mono emitting the claims map if valid, or an error if invalid
     */
    Mono<Map<String, Object>> verifyToken(String token);
}
