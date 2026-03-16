package com.ggar.hibiki.features.devices.handler.query;

import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.UserId;
import com.ggar.hibiki.features.devices.port.DeviceRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Use case responsible for retrieving all recorded devices (both ACTIVE and
 * REVOKED)
 * associated with a specific user.
 */
@Slf4j
@Service
public class GetDevicesQueryHandlerImpl implements GetDevicesQueryHandler {

    private final DeviceRepository deviceRepository;

    public GetDevicesQueryHandlerImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Mono<List<Device>> handle(GetDevicesQueryHandler.Get query) {
        UserId userId = UserId.of(query.userId());
        log.info("Fetching devices for user {}", userId);

        return deviceRepository.findByUserId(userId).collectList();
    }
}
