package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.identity.dto.RefreshAuthRequest;
import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.orchestrator.dto.RefreshRequestDTO;
import com.ggar.hibiki.core.orchestrator.mapper.RefreshRequestMapper;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.devices.dto.ValidateDeviceLoginQuery;
import com.ggar.hibiki.features.devices.model.DeviceId;
import com.ggar.hibiki.features.devices.model.IdentityContext;
import com.ggar.hibiki.features.devices.model.User;
import com.ggar.hibiki.features.devices.model.UserId;
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

    public Mono<AuthResponse> execute(RefreshRequestDTO requestDto, String ip, String userAgent) {
        RefreshAuthRequest refreshCommand = mapper.toCommand(requestDto);

        // 1: Renew tokens
        return Mono.from(mediator.send(refreshCommand)).flatMap(authResponse -> {
            // 2: Validate the device
            var identityContext = IdentityContext.builder()
                    .user(User.builder().id(UserId.of(authResponse.getUserId())).build())
                    .build();

            ValidateDeviceLoginQuery deviceQuery = ValidateDeviceLoginQuery.builder()
                    .identityContext(identityContext)
                    .deviceId(DeviceId.of(requestDto.getDeviceId()))
                    .ip(ip)
                    .userAgent(userAgent)
                    .build();

            return Mono.from(mediator.send(deviceQuery)).thenReturn(authResponse);
        });
    }
}
