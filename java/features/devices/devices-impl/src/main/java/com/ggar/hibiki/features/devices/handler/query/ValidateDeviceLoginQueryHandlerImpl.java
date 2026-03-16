package com.ggar.hibiki.features.devices.handler.query;

import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.DeviceStatus;
import com.ggar.hibiki.features.devices.model.UserId;
import com.ggar.hibiki.features.devices.port.DeviceRepository;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Use case responsible for verifying the validity and ownership of a given
 * device
 * during the login process. It also updates the last seen timestamp if
 * successful.
 */
@Service
@Slf4j
public class ValidateDeviceLoginQueryHandlerImpl implements ValidateDeviceLoginQueryHandler {

    private final DeviceRepository deviceRepository;

    public ValidateDeviceLoginQueryHandlerImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Mono<Boolean> handle(ValidateDeviceLoginQueryHandler.Validate query) {
        UserId userId = UserId.of(query.userId());
        DeviceId deviceId = DeviceId.of(query.deviceId());

        log.info("Validating device login for user {} and device {}", userId, deviceId);

        return deviceRepository
                .findById(deviceId)
                .flatMap(device -> {
                    if (device.getStatus() == DeviceStatus.REVOKED) {
                        log.warn("Login attempt on revoked device: {}", deviceId);
                        return Mono.error(new RuntimeException("Device is revoked"));
                    }

                    if (!device.getUserId().equals(userId)) {
                        log.warn("Device {} does not belong to user {}", deviceId, userId);
                        return Mono.error(new RuntimeException("Device ownership mismatch"));
                    }

                    var updatedDevice = device.toBuilder()
                            .lastSeenAt(Instant.now())
                            .lastIp(query.ip())
                            .userAgent(query.userAgent())
                            .build();

                    return deviceRepository.save(updatedDevice).thenReturn(true);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Device not found. Please register the device first.")));
    }
}
