package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.orchestrator.dto.RevokeDeviceRequestDTO;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.devices.dto.RevokeDeviceCommand;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.IdentityContext;
import com.ggar.hibiki.features.devices.model.User;
import com.ggar.hibiki.features.devices.model.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RevokeDeviceUseCase extends BaseOrchestratorUseCase {

    public RevokeDeviceUseCase(Mediator mediator) {
        super(mediator);
    }

    public Mono<Void> execute(RevokeDeviceRequestDTO request) {
        var identityContext = IdentityContext.builder()
                .user(User.builder().id(UserId.of(request.getUserId())).build())
                .build();

        RevokeDeviceCommand command = RevokeDeviceCommand.builder()
                .identityContext(identityContext)
                .deviceId(DeviceId.of(request.getDeviceId()))
                .build();
        return Mono.from(mediator.send(command));
    }
}
