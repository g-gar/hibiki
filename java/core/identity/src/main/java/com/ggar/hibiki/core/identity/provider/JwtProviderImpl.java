package com.ggar.hibiki.core.identity.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProviderImpl implements JwtProvider {

    private final SecretKey key;
    private final long expirationTime;

    public JwtProviderImpl(
            @Value("${hibiki.security.jwt.secret:defaultSecretKeyWithAtLeast256BitsForHS256Algorithm}") String secret,
            @Value("${hibiki.security.jwt.expiration:3600000}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    @Override
    public Mono<String> generateToken(String userId) {
        return Mono.fromCallable(() -> {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationTime);

            return Jwts.builder()
                    .subject(userId)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(key)
                    .compact();
        });
    }

    @Override
    public Mono<Claims> validateToken(String token) {
        return Mono.fromCallable(() -> Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload());
    }
}
