package com.ggar.hibiki.features.devices.infrastructure.persistence;

import com.ggar.hibiki.features.devices.infrastructure.persistence.mapper.DeviceMapper;
import com.ggar.hibiki.features.devices.infrastructure.persistence.repository.ReactiveNeo4jDeviceRepository;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.port.DeviceRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Infrastructure adapter implementing the {@link DeviceRepository} port.
 * Responsible for mapping between the Domain model {@link Device}
 * and the Neo4j Entity representation before interacting with the underlying
 * database.
 */
@Component
public class DeviceRepositoryImpl implements DeviceRepository {

    private final ReactiveNeo4jDeviceRepository repository;
    private final DeviceMapper mapper;

    public DeviceRepositoryImpl(ReactiveNeo4jDeviceRepository repository, DeviceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Device> save(Device device) {
        return Mono.just(device).map(mapper::toEntity).flatMap(repository::save).map(mapper::toDomain);
    }

    @Override
    public Mono<Device> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Flux<Device> findByUserId(UUID userId) {
        return repository.findByUserId(userId).map(mapper::toDomain);
    }
}
