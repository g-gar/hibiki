package com.ggar.hibiki.core.identity.provider;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

public interface JwtProvider {
    Mono<String> generateToken(String userId);

    Mono<Claims> validateToken(String token);
}
