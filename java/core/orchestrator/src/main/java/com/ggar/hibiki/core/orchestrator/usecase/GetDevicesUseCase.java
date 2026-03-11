package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.orchestrator.dto.DeviceDTO;
import com.ggar.hibiki.core.orchestrator.mapper.DeviceOrchestratorMapper;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.devices.dto.GetDevicesQuery;
import com.ggar.hibiki.features.devices.model.IdentityContext;
import com.ggar.hibiki.features.devices.model.User;
import com.ggar.hibiki.features.devices.model.UserId;
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
        var identityContext = IdentityContext.builder()
                .user(User.builder().id(UserId.of(userId)).build())
                .build();
        return Mono.from(mediator.send(GetDevicesQuery.builder()
                        .identityContext(identityContext)
                        .build()))
                .map(mapper::toDtoList);
    }
}
