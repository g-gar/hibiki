package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.dto.AuthResponse;
import com.ggar.hibiki.core.identity.dto.LoginRequest;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.identity.service.LoginCommandHandler;
import com.ggar.hibiki.packages.jwt.signer.JwtSigner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * Command handler responsible for authenticating a user.
 * Evaluates credentials and upon success, generates an access token and a
 * refresh token.
 * Device validation is now outsourced to the Orchestrator layer.
 */
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
        return userRepository
                .findByUsername(request.getUsername())
                .filter(user -> user.getPassword().equals(request.getPassword()))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")))
                .flatMap(user -> Mono.zip(
                                jwtSigner.generateToken(user.getId().toString()),
                                jwtSigner.generateToken(
                                        user.getId().toString(),
                                        java.util.Map.of(
                                                "type",
                                                "refresh",
                                                "deviceId",
                                                request.getDeviceId() != null
                                                        ? request.getDeviceId().toString()
                                                        : ""),
                                        30L * 24L * 60L * 60L * 1000L))
                        .map((Tuple2<String, String> tokens) -> AuthResponse.builder()
                                .token(tokens.getT1())
                                .refreshToken(tokens.getT2())
                                .userId(user.getId())
                                .username(user.getUsername())
                                .build()));
    }
}
