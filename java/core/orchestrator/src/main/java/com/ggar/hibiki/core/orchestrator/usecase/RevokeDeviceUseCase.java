package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.orchestrator.dto.RevokeDeviceRequestDTO;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.devices.handler.command.RevokeDeviceCommandHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RevokeDeviceUseCase extends BaseOrchestratorUseCase {

    public RevokeDeviceUseCase(Mediator mediator) {
        super(mediator);
    }

    public Mono<Void> execute(RevokeDeviceRequestDTO request) {
        RevokeDeviceCommandHandler.Revoke command =
                new RevokeDeviceCommandHandler.Revoke(request.getUserId(), request.getDeviceId());
        return Mono.from(mediator.send(command));
    }
}
