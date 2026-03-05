package com.ggar.hibiki.features.devices.usecase;

import com.ggar.hibiki.core.shared.auth.query.ValidateDeviceLoginQuery;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.devices.domain.model.DeviceStatus;
import com.ggar.hibiki.features.devices.domain.ports.DeviceRepository;
import com.ggar.hibiki.features.devices.infrastructure.logging.DevicesLogger;
import java.time.Instant;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Use case responsible for verifying the validity and ownership of a given
 * device
 * during the login process. It also updates the last seen timestamp if
 * successful.
 */
@Service
public class ValidateDeviceLoginUseCase implements QueryHandler<ValidateDeviceLoginQuery, Boolean> {

    private final DeviceRepository deviceRepository;
    private final DevicesLogger devicesLogger;

    public ValidateDeviceLoginUseCase(DeviceRepository deviceRepository, DevicesLogger devicesLogger) {
        this.deviceRepository = deviceRepository;
        this.devicesLogger = devicesLogger;
    }

    @Override
    public Mono<Boolean> handle(ValidateDeviceLoginQuery query) {
        return deviceRepository
                .findById(query.getDeviceId())
                .flatMap(device -> {
                    if (device.getStatus() == DeviceStatus.REVOKED) {
                        devicesLogger.warn("Login attempt on revoked device: " + query.getDeviceId());
                        return Mono.error(new RuntimeException("Device is revoked"));
                    }

                    if (!device.getUserId().equals(query.getUserId())) {
                        devicesLogger.warn(
                                "Device " + query.getDeviceId() + " does not belong to user " + query.getUserId());
                        return Mono.error(new RuntimeException("Device ownership mismatch"));
                    }

                    var updatedDevice = device.toBuilder()
                            .lastSeenAt(Instant.now())
                            .lastIp(query.getIp())
                            .userAgent(query.getUserAgent())
                            .build();

                    return deviceRepository.save(updatedDevice).thenReturn(true);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Device not found. Please register the device first.")));
    }
}
