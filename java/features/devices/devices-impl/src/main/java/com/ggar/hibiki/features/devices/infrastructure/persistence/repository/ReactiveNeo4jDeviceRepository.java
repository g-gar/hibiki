package com.ggar.hibiki.features.devices.infrastructure.persistence.repository;

import com.ggar.hibiki.features.devices.infrastructure.persistence.entity.DeviceEntity;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ReactiveNeo4jDeviceRepository extends ReactiveCrudRepository<DeviceEntity, UUID> {
    Flux<DeviceEntity> findByUserId(UUID userId);
}
