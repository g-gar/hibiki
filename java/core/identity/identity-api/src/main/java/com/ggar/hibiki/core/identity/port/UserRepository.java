package com.ggar.hibiki.core.identity.port;

import com.ggar.hibiki.core.identity.model.User;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);

    Mono<User> findById(UUID id);

    Mono<User> findByUsername(String username);

    Mono<User> findByEmail(String email);
}
