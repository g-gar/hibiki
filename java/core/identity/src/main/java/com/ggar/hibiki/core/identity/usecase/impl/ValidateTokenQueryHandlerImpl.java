package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.model.ValidateTokenQuery;
import com.ggar.hibiki.core.identity.provider.JwtProvider;
import com.ggar.hibiki.core.identity.usecase.ValidateTokenQueryHandler;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ValidateTokenQueryHandlerImpl implements ValidateTokenQueryHandler {

    private final JwtProvider jwtProvider;

    public ValidateTokenQueryHandlerImpl(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<Claims> handle(ValidateTokenQuery query) {
        return jwtProvider.validateToken(query.getToken());
    }
}
