package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.identity.handler.command.RefreshAuthCommandHandler;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.orchestrator.dto.RefreshRequestDTO;
import com.ggar.hibiki.core.orchestrator.mapper.RefreshRequestMapper;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.devices.handler.query.ValidateDeviceLoginQueryHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * High-level Use Case orchestrating the Refresh Token flow.
 */
@Service
public class RefreshAuthUseCase extends BaseOrchestratorUseCase {

    private final RefreshRequestMapper mapper;

    public RefreshAuthUseCase(Mediator mediator, RefreshRequestMapper mapper) {
        super(mediator);
        this.mapper = mapper;
    }

    public Mono<User> execute(RefreshRequestDTO requestDto, String ip, String userAgent) {
        RefreshAuthCommandHandler.Refresh refreshCommand = mapper.toCommand(requestDto);

        // 1: Renew tokens
        return Mono.from(mediator.send(refreshCommand)).flatMap(authResponse -> {
            // 2: Validate the device
            ValidateDeviceLoginQueryHandler.Validate validationQuery = new ValidateDeviceLoginQueryHandler.Validate(
                    authResponse.getId(), requestDto.getDeviceId(), ip, userAgent);

            return Mono.from(mediator.send(validationQuery)).thenReturn(authResponse);
        });
    }
}
