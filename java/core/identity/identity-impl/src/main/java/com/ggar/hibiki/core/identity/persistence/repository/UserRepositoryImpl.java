package com.ggar.hibiki.core.identity.persistence.repository;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.persistence.mapper.UserMapper;
import com.ggar.hibiki.core.identity.port.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final ReactiveNeo4jUserRepository repository;
    private final UserMapper mapper;

    public UserRepositoryImpl(ReactiveNeo4jUserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> save(User user) {
        return repository.save(mapper.toEntity(user)).map(mapper::toDomain);
    }

    @Override
    public Mono<User> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }
}
