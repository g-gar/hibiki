package com.ggar.hibiki.features.devices.usecase;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.devices.domain.model.DeviceStatus;
import com.ggar.hibiki.features.devices.domain.ports.DeviceRepository;
import com.ggar.hibiki.features.devices.usecase.command.RevokeDeviceCommand;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Use case responsible for revoking access to a device.
 * A revoked device cannot be used for subsequent logins or token refreshes.
 */
@Service
public class RevokeDeviceUseCase implements CommandHandler<RevokeDeviceCommand, Void> {

    private final DeviceRepository deviceRepository;

    public RevokeDeviceUseCase(DeviceRepository deviceRepository) {
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
