package com.ggar.hibiki.core.identity.usecase.impl;

import com.ggar.hibiki.core.identity.model.SignupRequest;
import com.ggar.hibiki.core.identity.persistence.entity.UserEntity;
import com.ggar.hibiki.core.identity.persistence.repository.UserRepository;
import com.ggar.hibiki.core.identity.usecase.SignupCommandHandler;
import java.util.Collections;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SignupCommandHandlerImpl implements SignupCommandHandler {

    private final UserRepository userRepository;

    public SignupCommandHandlerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Void> handle(SignupRequest request) {
        return userRepository
                .findByUsername(request.getUsername())
                .flatMap(user -> Mono.error(new RuntimeException("User already exists"))) // TODO: Custom exception
                .switchIfEmpty(userRepository
                        .findByEmail(request.getEmail())
                        .flatMap(user -> Mono.error(new RuntimeException("Email already exists"))))
                .then(Mono.defer(() -> {
                    UserEntity entity = new UserEntity();
                    entity.setUsername(request.getUsername());
                    entity.setEmail(request.getEmail());
                    entity.setPassword(request.getPassword()); // TODO: Hash password
                    entity.setRoles(Collections.singleton("ROLE_USER"));
                    entity.setTwoFactorEnabled(false);
                    return userRepository.save(entity);
                }))
                .then();
    }
}
