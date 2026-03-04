package com.ggar.hibiki.core.orchestrator.devices;

import com.ggar.hibiki.core.orchestrator.devices.model.RevokeDeviceRequestDTO;
import com.ggar.hibiki.core.orchestrator.shared.BaseOrchestratorUseCase;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.devices.usecase.command.RevokeDeviceCommand;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RevokeDeviceUseCase extends BaseOrchestratorUseCase {

    public RevokeDeviceUseCase(Mediator mediator) {
        super(mediator);
    }

    public Mono<Void> execute(RevokeDeviceRequestDTO request) {
        return mediator.send(RevokeDeviceCommand.builder()
                .userId(request.getUserId())
                .deviceId(request.getDeviceId())
                .build());
    }
}
