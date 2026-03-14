package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.dto.SignupRequest;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.identity.service.SignupCommandHandler;
import java.util.Collections;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SignupCommandHandlerImpl implements SignupCommandHandler {

    private final UserRepository userRepository;
    private final com.ggar.hibiki.core.shared.event.EventBus eventBus;

    public SignupCommandHandlerImpl(
            UserRepository userRepository, com.ggar.hibiki.core.shared.event.EventBus eventBus) {
        this.userRepository = userRepository;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<Void> handle(SignupRequest request) {
        return userRepository
                .findByUsername(request.getUsername())
                .flatMap(
                        user -> Mono.<User>error(new RuntimeException("User already exists"))) // TODO: Custom exception
                .switchIfEmpty(userRepository
                        .findByEmail(request.getEmail())
                        .flatMap(user -> Mono.<User>error(new RuntimeException("Email already exists"))))
                .then(Mono.defer(() -> {
                    User user = User.builder()
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .password(request.getPassword()) // TODO: Hash password
                            .roles(Collections.singleton("ROLE_USER")) // TODO: change this
                            .twoFactorEnabled(false)
                            .build();
                    return userRepository.save(user);
                }))
                .flatMap(user -> eventBus.publish(com.ggar.hibiki.core.identity.event.UserSignedUpEvent.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build()))
                .then();
    }
}
