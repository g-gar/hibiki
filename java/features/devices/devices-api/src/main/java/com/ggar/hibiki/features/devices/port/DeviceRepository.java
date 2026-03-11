package com.ggar.hibiki.features.devices.port;

import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.UserId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Persistence port for managing {@link Device} entities.
 * Defines the contract that infrastructure adapters must implement
 * to store and retrieve devices from the database.
 */
public interface DeviceRepository {

    /**
     * Saves a new device or updates an existing one.
     *
     * @param device the device domain model to save
     * @return a {@link Mono} emitting the saved device
     */
    Mono<Device> save(Device device);

    /**
     * Finds a device by its unique identifier.
     *
     * @param id the unique device identifier
     * @return a {@link Mono} emitting the found device, or empty if not found
     */
    Mono<Device> findById(DeviceId id);

    /**
     * Retrieves all devices associated with a specific user.
     *
     * @param userId the user identifier
     * @return a {@link Flux} emitting the user's devices
     */
    Flux<Device> findByUserId(UserId userId);
}
