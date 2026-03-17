package com.ggar.hibiki.features.devices.handler.command;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.DeviceStatus;
import com.ggar.hibiki.features.devices.model.UserId;
import com.ggar.hibiki.features.devices.port.DeviceRepository;
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
    private final EventBus eventBus;

    public RevokeDeviceCommandHandlerImpl(DeviceRepository deviceRepository, EventBus eventBus) {
        this.deviceRepository = deviceRepository;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<Void> handle(RevokeDeviceCommandHandler.Revoke command) {
        DeviceId deviceId = DeviceId.of(command.deviceId());
        UserId userId = UserId.of(command.userId());
        log.info("Revoking device {}", deviceId);

        return deviceRepository
                .findById(deviceId)
                .filter(device -> device.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(new RuntimeException("Device not found or ownership mismatch")))
                .flatMap(device -> {
                    var revokedDevice =
                            device.toBuilder().status(DeviceStatus.REVOKED).build();
                    return deviceRepository
                            .save(revokedDevice)
                            .doOnNext(revoked -> eventBus.publish(new RevokeDeviceCommandHandler.Revoked(
                                    revoked.getId().getValue(),
                                    revoked.getUserId().getValue())));
                })
                .then();
    }
}
