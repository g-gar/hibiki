package com.ggar.hibiki.features.devices.infrastructure.handler;

import com.ggar.hibiki.features.devices.dto.RevokeDeviceCommand;
import com.ggar.hibiki.features.devices.model.DeviceStatus;
import com.ggar.hibiki.features.devices.port.DeviceRepository;
import com.ggar.hibiki.features.devices.service.RevokeDeviceCommandHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Use case responsible for revoking access to a device.
 * A revoked device cannot be used for subsequent logins or token refreshes.
 */
@Service
public class RevokeDeviceCommandHandlerImpl implements RevokeDeviceCommandHandler {

    private final DeviceRepository deviceRepository;

    public RevokeDeviceCommandHandlerImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Mono<Void> handle(RevokeDeviceCommand command) {
        return deviceRepository
                .findById(command.getDeviceId())
                .filter(device -> device.getUserId().equals(command.getUserId()))
                .switchIfEmpty(Mono.error(new RuntimeException("Device not found or ownership mismatch")))
                .flatMap(device -> {
                    var revokedDevice =
                            device.toBuilder().status(DeviceStatus.REVOKED).build();
                    return deviceRepository.save(revokedDevice);
                })
                .then();
    }
}
