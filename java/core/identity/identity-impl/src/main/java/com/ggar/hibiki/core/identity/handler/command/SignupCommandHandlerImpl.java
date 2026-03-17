package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link SignupCommandHandler}.
 * This service handles user registration and emits a {@link SignupCommandHandler.SignedUp} event.
 */
@Service
@RequiredArgsConstructor
public class SignupCommandHandlerImpl implements SignupCommandHandler {

    private final UserRepository userRepository;
    private final EventBus eventBus;

    @Override
    public Mono<User> handle(Signup signup) {
        return Mono.zip(
                        userRepository
                                .findByUsername(signup.username())
                                .map(user -> true)
                                .defaultIfEmpty(false),
                        userRepository
                                .findByEmail(signup.email())
                                .map(user -> true)
                                .defaultIfEmpty(false))
                .flatMap(tuple -> {
                    boolean usernameExists = tuple.getT1();
                    boolean emailExists = tuple.getT2();

                    if (usernameExists) {
                        return Mono.error(new RuntimeException("User already exists"));
                    }
                    if (emailExists) {
                        return Mono.error(new RuntimeException("Email already exists"));
                    }

                    User user = User.builder()
                            .username(signup.username())
                            .email(signup.email())
                            .password(signup.password()) // TODO: Hash password
                            .roles(Collections.singleton("ROLE_USER")) // TODO: change this
                            .twoFactorEnabled(false)
                            .build();

                    return userRepository.save(user);
                })
                .flatMap(user -> eventBus.publish(new SignedUp(user.getId(), user.getUsername(), user.getEmail()))
                        .thenReturn(user));
    }
}
