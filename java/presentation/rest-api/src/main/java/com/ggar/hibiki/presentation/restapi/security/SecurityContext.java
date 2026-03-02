package com.ggar.hibiki.presentation.restapi.security;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

public class SecurityContext {

    public static Mono<String> getUserId(ServerWebExchange exchange) {
        return Mono.justOrEmpty((String) exchange.getAttribute("userId"));
    }

    @SuppressWarnings("unchecked")
    public static Mono<Map<String, Object>> getClaims(ServerWebExchange exchange) {
        return Mono.justOrEmpty((Map<String, Object>) exchange.getAttribute("userClaims"));
    }
}
