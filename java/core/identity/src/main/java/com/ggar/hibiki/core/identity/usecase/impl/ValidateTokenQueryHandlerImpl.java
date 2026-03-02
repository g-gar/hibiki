package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.model.ValidateTokenQuery;
import com.ggar.hibiki.core.identity.usecase.ValidateTokenQueryHandler;
import com.ggar.hibiki.packages.jwt.verifier.JwtVerifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ValidateTokenQueryHandlerImpl implements ValidateTokenQueryHandler {

    private final JwtVerifier jwtVerifier;

    public ValidateTokenQueryHandlerImpl(JwtVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    public Mono<Map<String, Object>> handle(ValidateTokenQuery query) {
        return jwtVerifier.verifyToken(query.getToken());
    }
}
