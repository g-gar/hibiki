package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.identity.model.RefreshAuthRequest;
import com.ggar.hibiki.core.identity.usecase.RefreshAuthCommandHandler;
import com.ggar.hibiki.packages.jwt.signer.JwtSigner;
import com.ggar.hibiki.packages.jwt.verifier.JwtVerifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Handler responsible for processing refresh token requests.
 * It verifies the provided refresh token and type, issuing new access and
 * refresh tokens.
 * Device validation is outsourced to the Orchestrator layer.
 */
@Service
public class RefreshAuthCommandHandlerImpl implements RefreshAuthCommandHandler {

        private final JwtVerifier jwtVerifier;
        private final JwtSigner jwtSigner;

        public RefreshAuthCommandHandlerImpl(JwtVerifier jwtVerifier, JwtSigner jwtSigner) {
                this.jwtVerifier = jwtVerifier;
                this.jwtSigner = jwtSigner;
        }

        @Override
        public Mono<AuthResponse> handle(RefreshAuthRequest request) {
                return jwtVerifier.verifyToken(request.getRefreshToken())
                                .flatMap(claims -> {
                                        String userId = (String) claims.get("sub");
                                        String tokenType = (String) claims.get("type");
                                        String tokenDeviceId = (String) claims.get("deviceId");

                                        if (!"refresh".equals(tokenType)) {
                                                return Mono.error(new RuntimeException(
                                                                "Invalid token type. Expected refresh token."));
                                        }

                                        return Mono.zip(
                                                        jwtSigner.generateToken(userId),
                                                        jwtSigner.generateToken(userId,
                                                                        Map.of("type", "refresh", "deviceId",
                                                                                        request.getDeviceId() != null
                                                                                                        ? request.getDeviceId()
                                                                                                        : tokenDeviceId),
                                                                        30L * 24L * 60L * 60L * 1000L))
                                                        .map(tokens -> AuthResponse.builder()
                                                                        .token(tokens.getT1())
                                                                        .refreshToken(tokens.getT2())
                                                                        .userId(userId)
                                                                        .username("fetched_from_db_or_cache")
                                                                        .build());
                                });
        }
}
