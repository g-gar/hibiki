package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateProfileCommandHandlerImpl implements UpdateProfileCommandHandler {

    private final UserRepository userRepository;
    private final EventBus eventBus;

    @Override
    public Mono<User> handle(Update update) {
        return userRepository
                .findByUsername(update.username())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user -> {
                    user.setEmail(update.email());
                    return userRepository.save(user);
                })
                .flatMap(savedUser -> eventBus.publish(
                                new Updated(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail()))
                        .thenReturn(savedUser));
    }
}
