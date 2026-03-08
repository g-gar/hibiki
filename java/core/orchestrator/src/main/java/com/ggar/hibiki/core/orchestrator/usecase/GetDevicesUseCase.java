package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.orchestrator.dto.DeviceDTO;
import com.ggar.hibiki.core.orchestrator.mapper.DeviceOrchestratorMapper;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.devices.dto.GetDevicesQuery;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetDevicesUseCase extends BaseOrchestratorUseCase {

    private final DeviceOrchestratorMapper mapper;

    public GetDevicesUseCase(Mediator mediator, DeviceOrchestratorMapper mapper) {
        super(mediator);
        this.mapper = mapper;
    }

    public Mono<List<DeviceDTO>> execute(UUID userId) {
        return mediator.send(GetDevicesQuery.builder().userId(userId).build()).map(mapper::toDtoList);
    }
}
