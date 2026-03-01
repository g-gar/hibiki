package com.ggar.hibiki.presentation.restapi.security;

import io.jsonwebtoken.Claims;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class SecurityContext {

    public static Mono<String> getUserId(ServerWebExchange exchange) {
        return Mono.justOrEmpty((String) exchange.getAttribute("userId"));
    }

    public static Mono<Claims> getClaims(ServerWebExchange exchange) {
        return Mono.justOrEmpty((Claims) exchange.getAttribute("userClaims"));
    }
}
