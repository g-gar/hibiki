package com.ggar.hibiki.features.devices.infrastructure.handler;

import com.ggar.hibiki.features.devices.dto.RegisterDeviceCommand;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.DeviceStatus;
import com.ggar.hibiki.features.devices.port.DeviceRepository;
import com.ggar.hibiki.features.devices.service.RegisterDeviceCommandHandler;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Use case responsible for registering a new user device
 * or updating the metadata (IP, UserAgent, LastSeenAt) of an existing active
 * one.
 */
@Service
@Slf4j
public class RegisterDeviceCommandHandlerImpl implements RegisterDeviceCommandHandler {

    private final DeviceRepository deviceRepository;

    public RegisterDeviceCommandHandlerImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Mono<Device> handle(RegisterDeviceCommand command) {
        return deviceRepository
                .findById(command.getId())
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
                    Device newDevice = Device.builder()
                            .id(command.getId())
                            .userId(command.getIdentityContext().getUser().getId())
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
