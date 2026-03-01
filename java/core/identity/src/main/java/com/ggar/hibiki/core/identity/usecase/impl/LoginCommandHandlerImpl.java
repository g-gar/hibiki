package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.identity.model.LoginRequest;
import com.ggar.hibiki.core.identity.persistence.repository.UserRepository;
import com.ggar.hibiki.core.identity.provider.JwtProvider;
import com.ggar.hibiki.core.identity.usecase.LoginCommandHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoginCommandHandlerImpl implements LoginCommandHandler {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public LoginCommandHandlerImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<AuthResponse> handle(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> user.getPassword().equals(request.getPassword())) // TODO: Use password hashing
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")))
                .flatMap(user -> jwtProvider.generateToken(user.getId())
                        .map(token -> AuthResponse.builder()
                                .token(token)
                                .userId(user.getId())
                                .username(user.getUsername())
                                .build()));
    }
}
