package com.ggar.hibiki.core.identity.handler.query;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.packages.jwt.verifier.JwtVerifier;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ValidateTokenQueryHandlerImpl implements ValidateTokenQueryHandler {

    private final JwtVerifier jwtVerifier;

    @Override
    public Mono<User> handle(Validate validate) {
        return jwtVerifier.verifyToken(validate.token()).map(claims -> {
            // TODO: Map claims to User model properly.
            // For now returning a placeholder as actual user fetching/mapping is needed.
            return User.builder()
                    .id(UUID.fromString((String) claims.get("sub")))
                    .username("placeholder")
                    .email("placeholder@example.com")
                    .build();
        });
    }
}
