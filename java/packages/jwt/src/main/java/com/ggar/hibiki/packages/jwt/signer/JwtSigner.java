package com.ggar.hibiki.packages.jwt.signer;

import reactor.core.publisher.Mono;

import java.util.Map;

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
}
