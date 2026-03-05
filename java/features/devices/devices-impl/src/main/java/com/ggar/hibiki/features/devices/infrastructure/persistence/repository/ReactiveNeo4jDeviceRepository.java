package com.ggar.hibiki.features.devices.infrastructure.persistence.repository;

import com.ggar.hibiki.features.devices.infrastructure.persistence.entity.DeviceEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ReactiveNeo4jDeviceRepository extends ReactiveCrudRepository<DeviceEntity, String> {
    Flux<DeviceEntity> findByUserId(String userId);
}
