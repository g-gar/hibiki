package com.ggar.hibiki.features.devices.usecase.impl;

import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.devices.domain.model.Device;
import com.ggar.hibiki.features.devices.domain.ports.DeviceRepository;
import com.ggar.hibiki.features.devices.usecase.query.GetDevicesQuery;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Use case responsible for retrieving all recorded devices (both ACTIVE and
 * REVOKED)
 * associated with a specific user.
 */
@Service
public class GetDevicesUseCase implements QueryHandler<GetDevicesQuery, List<Device>> {

    private final DeviceRepository deviceRepository;

    public GetDevicesUseCase(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Mono<List<Device>> handle(GetDevicesQuery query) {
        return deviceRepository.findByUserId(query.getUserId())
                .collectList();
    }
}
