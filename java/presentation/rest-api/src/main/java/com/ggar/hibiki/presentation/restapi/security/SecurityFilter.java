package com.ggar.hibiki.presentation.restapi.security;

import com.ggar.hibiki.core.identity.handler.query.ValidateTokenQueryHandler;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class SecurityFilter implements WebFilter {

    private final Mediator mediator;
    private static final String BEARER_PREFIX = "Bearer ";

    public SecurityFilter(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Skip auth for login/signup endpoints
        if (path.startsWith("/api/auth/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        return Mono.from(mediator.send(new ValidateTokenQueryHandler.Validate(token)))
                .flatMap(user -> {
                    exchange.getAttributes().put("user", user);
                    exchange.getAttributes().put("userId", user.getId());
                    return chain.filter(exchange);
                })
                .onErrorResume(e -> chain.filter(exchange));
    }
}
