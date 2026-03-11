package com.ggar.hibiki.features.devices.infrastructure.handler;

import com.ggar.hibiki.features.devices.dto.RevokeDeviceCommand;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.DeviceStatus;
import com.ggar.hibiki.features.devices.model.UserId;
import com.ggar.hibiki.features.devices.port.DeviceRepository;
import com.ggar.hibiki.features.devices.service.RevokeDeviceCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Use case responsible for revoking access to a device.
 * A revoked device cannot be used for subsequent logins or token refreshes.
 */
@Service
public class RevokeDeviceCommandHandlerImpl implements RevokeDeviceCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(RevokeDeviceCommandHandlerImpl.class);

    private final DeviceRepository deviceRepository;

    public RevokeDeviceCommandHandlerImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Mono<Void> handle(RevokeDeviceCommand command) {
        DeviceId deviceId = DeviceId.of(command.getDeviceId());
        UserId userId = UserId.of(command.getUserId());
        log.info("Revoking device {}", deviceId);

        return deviceRepository
                .findById(deviceId)
                .filter(device -> device.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(new RuntimeException("Device not found or ownership mismatch")))
                .flatMap(device -> {
                    var revokedDevice =
                            device.toBuilder().status(DeviceStatus.REVOKED).build();
                    return deviceRepository.save(revokedDevice);
                })
                .then();
    }
}
