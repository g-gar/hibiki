package com.ggar.hibiki.packages.jwt.verifier;

import com.ggar.hibiki.packages.jwt.exception.JwtException;
import com.ggar.hibiki.packages.jwt.logging.Logger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.crypto.SecretKey;
import reactor.core.publisher.Mono;

/**
 * Concrete implementation of {@link JwtVerifier} using the JJWT library.
 */
public class JjwtVerifier implements JwtVerifier {

    private final SecretKey key;
    private final Logger logger;

    /**
     * Constructs a new {@link JjwtVerifier}.
     *
     * @param secret the secret string used to verify the HMAC signature
     * @param logger the logger instance
     */
    public JjwtVerifier(String secret, Logger logger) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.logger = logger;
    }

    @Override
    public Mono<Map<String, Object>> verifyToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                logger.debug("Verifying JWT token");
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                return (Map<String, Object>) claims;
            } catch (SignatureException ex) {
                logger.warn("Invalid JWT signature");
                throw new JwtException("Invalid JWT signature", ex);
            } catch (MalformedJwtException ex) {
                logger.warn("Invalid JWT token structure");
                throw new JwtException("Invalid JWT token structure", ex);
            } catch (ExpiredJwtException ex) {
                logger.warn("Expired JWT token");
                throw new JwtException("Expired JWT token", ex);
            } catch (UnsupportedJwtException ex) {
                logger.warn("Unsupported JWT token");
                throw new JwtException("Unsupported JWT token", ex);
            } catch (IllegalArgumentException ex) {
                logger.warn("JWT claims string is empty");
                throw new JwtException("JWT claims string is empty", ex);
            } catch (Exception e) {
                logger.error("Failed to verify JWT token", e);
                throw new JwtException("Failed to verify JWT token", e);
            }
        });
    }
}
