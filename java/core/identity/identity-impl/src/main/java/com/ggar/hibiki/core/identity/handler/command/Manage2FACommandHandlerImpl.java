package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class Manage2FACommandHandlerImpl implements Manage2FACommandHandler {

    private final UserRepository userRepository;
    private final EventBus eventBus;

    @Override
    public Mono<Void> handle(Manage2FACommandHandler.Manage manage) {
        return userRepository
                .findById(UUID.fromString(manage.userId()))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user -> {
                    user.setTwoFactorEnabled(manage.enabled());
                    return userRepository.save(user);
                })
                .flatMap(user ->
                        eventBus.publish(new Manage2FACommandHandler.Managed(user.getId(), user.isTwoFactorEnabled())))
                .then();
    }
}
