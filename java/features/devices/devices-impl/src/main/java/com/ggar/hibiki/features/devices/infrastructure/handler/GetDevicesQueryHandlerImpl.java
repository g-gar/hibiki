package com.ggar.hibiki.features.devices.infrastructure.handler;

import com.ggar.hibiki.features.devices.dto.GetDevicesQuery;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.port.DeviceRepository;
import com.ggar.hibiki.features.devices.service.GetDevicesQueryHandler;
import java.util.List;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Use case responsible for retrieving all recorded devices (both ACTIVE and
 * REVOKED)
 * associated with a specific user.
 */
@Service
public class GetDevicesQueryHandlerImpl implements GetDevicesQueryHandler {

    private final DeviceRepository deviceRepository;

    public GetDevicesQueryHandlerImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Mono<List<Device>> handle(GetDevicesQuery query) {
        return deviceRepository.findByUserId(query.getUserId()).collectList();
    }
}
