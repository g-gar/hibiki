package com.ggar.hibiki.packages.jwt.signer;

import com.ggar.hibiki.packages.jwt.exception.JwtException;
import com.ggar.hibiki.packages.jwt.logging.Logger;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import reactor.core.publisher.Mono;

/**
 * Concrete implementation of {@link JwtSigner} using the JJWT library.
 */
public class JjwtSigner implements JwtSigner {

    private final SecretKey key;
    private final long expirationTime;
    private final Logger logger;

    /**
     * Constructs a new {@link JjwtSigner}.
     *
     * @param secret         the secret string to generate the HMAC key
     * @param expirationTime the token validity duration in milliseconds
     * @param logger         the logger instance to use
     */
    public JjwtSigner(String secret, long expirationTime, Logger logger) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
        this.logger = logger;
    }

    @Override
    public Mono<String> generateToken(String subject) {
        return generateToken(subject, new HashMap<>(), this.expirationTime);
    }

    @Override
    public Mono<String> generateToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, this.expirationTime);
    }

    @Override
    public Mono<String> generateToken(String subject, long expirationTime) {
        return generateToken(subject, new HashMap<>(), expirationTime);
    }

    @Override
    public Mono<String> generateToken(String subject, Map<String, Object> claims, long expirationTime) {
        return Mono.fromCallable(() -> {
            try {
                logger.debug("Generating JWT token for subject: {} with duration {} ms", subject, expirationTime);
                Date now = new Date();
                Date expiryDate = new Date(now.getTime() + expirationTime);

                return Jwts.builder()
                        .subject(subject)
                        .claims(claims)
                        .issuedAt(now)
                        .expiration(expiryDate)
                        .signWith(key)
                        .compact();
            } catch (Exception e) {
                logger.error("Failed to generate JWT token for subject: {}", subject, e);
                throw new JwtException("Failed to generate JWT token", e);
            }
        });
    }
}
