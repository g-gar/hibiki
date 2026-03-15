package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.identity.model.AuthContext;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.packages.jwt.signer.JwtSigner;
import com.ggar.hibiki.packages.jwt.verifier.JwtVerifier;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Handler responsible for processing refresh token requests.
 */
@Service
@RequiredArgsConstructor
public class RefreshAuthCommandHandlerImpl implements RefreshAuthCommandHandler {

    private final UserRepository userRepository;
    private final JwtVerifier jwtVerifier;
    private final JwtSigner jwtSigner;
    private final EventBus eventBus;

    @Override
    public Mono<User> handle(Refresh refresh) {
        return jwtVerifier.verifyToken(refresh.refreshToken()).flatMap(claims -> {
            String userId = (String) claims.get("sub");
            String tokenType = (String) claims.get("type");
            String tokenDeviceId = (String) claims.get("deviceId");

            if (!"refresh".equals(tokenType)) {
                return Mono.error(new RuntimeException("Invalid token type. Expected refresh token."));
            }

            return userRepository
                    .findById(UUID.fromString(userId))
                    .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                    .flatMap(user -> Mono.zip(
                                    jwtSigner.generateToken(userId),
                                    jwtSigner.generateToken(
                                            userId,
                                            Map.of(
                                                    "type",
                                                    "refresh",
                                                    "deviceId",
                                                    refresh.deviceId() != null ? refresh.deviceId() : tokenDeviceId),
                                            30L * 24L * 60L * 60L * 1000L))
                            .flatMap(tokens -> eventBus.publish(new Refreshed(user.getId(), refresh.deviceId()))
                                    .then(Mono.fromCallable(() -> {
                                        UUID existingAuthContextId = user.getAuthContext() != null
                                                ? user.getAuthContext().id()
                                                : null;
                                        user.setAuthContext(
                                                new AuthContext(existingAuthContextId, tokens.getT1(), tokens.getT2()));
                                        return user;
                                    }))));
        });
    }
}
