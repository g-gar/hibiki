package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.identity.model.AuthContext;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.packages.jwt.signer.JwtSigner;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * Command handler responsible for authenticating a user.
 */
@Service
@RequiredArgsConstructor
public class LoginCommandHandlerImpl implements LoginCommandHandler {

    private final UserRepository userRepository;
    private final JwtSigner jwtSigner;
    private final EventBus eventBus;

    @Override
    public Mono<User> handle(Login login) {
        return userRepository
                .findByUsername(login.username())
                .filter(user -> user.getPassword().equals(login.password())) // TODO: Use password encoder
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")))
                .flatMap(user -> Mono.zip(
                                jwtSigner.generateToken(user.getId().toString()),
                                jwtSigner.generateToken(
                                        user.getId().toString(),
                                        Map.of(
                                                "type",
                                                "refresh",
                                                "deviceId",
                                                login.deviceId() != null ? login.deviceId() : ""),
                                        30L * 24L * 60L * 60L * 1000L))
                        .flatMap((Tuple2<String, String> tokens) -> eventBus.publish(
                                        new LoggedIn(user.getId(), login.deviceId()))
                                .then(Mono.fromCallable(() -> {
                                    UUID existingAuthContextId = user.getAuthContext() != null
                                            ? user.getAuthContext().id()
                                            : null;
                                    user.setAuthContext(
                                            new AuthContext(existingAuthContextId, tokens.getT1(), tokens.getT2()));
                                    return user;
                                }))));
    }
}
