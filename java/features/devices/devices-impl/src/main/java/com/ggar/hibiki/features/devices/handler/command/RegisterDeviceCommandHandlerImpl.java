package com.ggar.hibiki.features.devices.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.DeviceStatus;
import com.ggar.hibiki.features.devices.model.UserId;
import com.ggar.hibiki.features.devices.port.DeviceRepository;
import java.time.Instant;
import java.util.UUID;
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
    private final EventBus eventBus;

    public RegisterDeviceCommandHandlerImpl(DeviceRepository deviceRepository, EventBus eventBus) {
        this.deviceRepository = deviceRepository;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<Device> handle(RegisterDeviceCommandHandler.Register command) {
        DeviceId deviceId = command.id() != null ? DeviceId.of(command.id()) : null;
        Mono<Device> deviceMono = deviceId != null ? deviceRepository.findById(deviceId) : Mono.empty();

        return deviceMono
                .flatMap(existingDevice -> {
                    if (existingDevice.getStatus() == DeviceStatus.REVOKED) {
                        return Mono.<Device>error(new RuntimeException(
                                "Cannot register a revoked device ID. Please generate a new ID on the client."));
                    }
                    var updated = existingDevice.toBuilder()
                            .lastIp(command.ip())
                            .userAgent(command.userAgent())
                            .lastSeenAt(Instant.now())
                            .build();
                    return deviceRepository
                            .save(updated)
                            .doOnNext(device -> eventBus.publish(new RegisterDeviceCommandHandler.Registered(
                                    device.getId().getValue(),
                                    device.getUserId().getValue(),
                                    device.getName())));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    Device newDevice = Device.builder()
                            .id(deviceId != null ? deviceId : DeviceId.of(UUID.randomUUID()))
                            .userId(UserId.of(command.userId()))
                            .name(command.name())
                            .type(command.type())
                            .status(DeviceStatus.ACTIVE)
                            .lastIp(command.ip())
                            .userAgent(command.userAgent())
                            .createdAt(Instant.now())
                            .lastSeenAt(Instant.now())
                            .build();
                    return deviceRepository
                            .save(newDevice)
                            .doOnNext(device -> eventBus.publish(new RegisterDeviceCommandHandler.Registered(
                                    device.getId().getValue(),
                                    device.getUserId().getValue(),
                                    device.getName())));
                }));
    }
}
