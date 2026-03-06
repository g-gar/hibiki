package com.ggar.hibiki.packages.jwt.verifier;

import com.ggar.hibiki.packages.jwt.exception.JwtException;
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
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Concrete implementation of {@link JwtVerifier} using the JJWT library.
 */
@Slf4j
public class JjwtVerifier implements JwtVerifier {

    private final SecretKey key;

    /**
     * Constructs a new {@link JjwtVerifier}.
     *
     * @param secret the secret string used to verify the HMAC signature
     */
    public JjwtVerifier(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Map<String, Object>> verifyToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                log.debug("Verifying JWT token");
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                return (Map<String, Object>) claims;
            } catch (SignatureException ex) {
                log.warn("Invalid JWT signature");
                throw new JwtException("Invalid JWT signature", ex);
            } catch (MalformedJwtException ex) {
                log.warn("Invalid JWT token structure");
                throw new JwtException("Invalid JWT token structure", ex);
            } catch (ExpiredJwtException ex) {
                log.warn("Expired JWT token");
                throw new JwtException("Expired JWT token", ex);
            } catch (UnsupportedJwtException ex) {
                log.warn("Unsupported JWT token");
                throw new JwtException("Unsupported JWT token", ex);
            } catch (IllegalArgumentException ex) {
                log.warn("JWT claims string is empty");
                throw new JwtException("JWT claims string is empty", ex);
            } catch (Exception e) {
                log.error("Failed to verify JWT token", e);
                throw new JwtException("Failed to verify JWT token", e);
            }
        });
    }
}
