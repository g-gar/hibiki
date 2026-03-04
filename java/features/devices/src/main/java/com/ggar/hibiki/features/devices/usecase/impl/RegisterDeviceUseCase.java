package com.ggar.hibiki.features.devices.usecase.impl;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.devices.domain.model.Device;
import com.ggar.hibiki.features.devices.domain.model.DeviceStatus;
import com.ggar.hibiki.features.devices.domain.ports.DeviceRepository;
import com.ggar.hibiki.features.devices.usecase.command.RegisterDeviceCommand;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Use case responsible for registering a new user device
 * or updating the metadata (IP, UserAgent, LastSeenAt) of an existing active
 * one.
 */
@Service
public class RegisterDeviceUseCase implements CommandHandler<RegisterDeviceCommand, Device> {

    private final DeviceRepository deviceRepository;

    public RegisterDeviceUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Mono<Device> handle(RegisterDeviceCommand command) {
        return deviceRepository.findById(command.getId())
                .flatMap(existingDevice -> {
                    if (existingDevice.getStatus() == DeviceStatus.REVOKED) {
                        return Mono.<Device>error(new RuntimeException(
                                "Cannot register a revoked device ID. Please generate a new ID on the client."));
                    }
                    var updated = existingDevice.toBuilder()
                            .lastIp(command.getIp())
                            .userAgent(command.getUserAgent())
                            .lastSeenAt(Instant.now())
                            .build();
                    return deviceRepository.save(updated);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    var newDevice = Device.builder()
                            .id(command.getId())
                            .userId(command.getUserId())
                            .name(command.getName())
                            .type(command.getType())
                            .status(DeviceStatus.ACTIVE)
                            .lastIp(command.getIp())
                            .userAgent(command.getUserAgent())
                            .createdAt(Instant.now())
                            .lastSeenAt(Instant.now())
                            .build();
                    return deviceRepository.save(newDevice);
                }));
    }
}
