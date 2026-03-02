package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.identity.model.LoginRequest;
import com.ggar.hibiki.core.identity.persistence.repository.UserRepository;
import com.ggar.hibiki.core.identity.usecase.LoginCommandHandler;
import com.ggar.hibiki.packages.jwt.signer.JwtSigner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoginCommandHandlerImpl implements LoginCommandHandler {

    private final UserRepository userRepository;
    private final JwtSigner jwtSigner;

    public LoginCommandHandlerImpl(UserRepository userRepository, JwtSigner jwtSigner) {
        this.userRepository = userRepository;
        this.jwtSigner = jwtSigner;
    }

    @Override
    public Mono<AuthResponse> handle(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> user.getPassword().equals(request.getPassword())) // TODO: Use password hashing
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")))
                .flatMap(user -> jwtSigner.generateToken(user.getId())
                        .map(token -> AuthResponse.builder()
                                .token(token)
                                .userId(user.getId())
                                .username(user.getUsername())
                                .build()));
    }
}
