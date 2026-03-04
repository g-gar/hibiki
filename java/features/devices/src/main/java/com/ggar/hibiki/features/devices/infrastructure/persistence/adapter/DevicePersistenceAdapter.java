package com.ggar.hibiki.features.devices.infrastructure.persistence.adapter;

import com.ggar.hibiki.features.devices.domain.model.Device;
import com.ggar.hibiki.features.devices.domain.ports.DeviceRepository;
import com.ggar.hibiki.features.devices.infrastructure.persistence.mapper.DeviceMapper;
import com.ggar.hibiki.features.devices.infrastructure.persistence.repository.ReactiveNeo4jDeviceRepository;
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
public class DevicePersistenceAdapter implements DeviceRepository {

    private final ReactiveNeo4jDeviceRepository repository;
    private final DeviceMapper mapper;

    public DevicePersistenceAdapter(ReactiveNeo4jDeviceRepository repository, DeviceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Device> save(Device device) {
        return Mono.just(device)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Device> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Device> findByUserId(String userId) {
        return repository.findByUserId(userId)
                .map(mapper::toDomain);
    }
}
