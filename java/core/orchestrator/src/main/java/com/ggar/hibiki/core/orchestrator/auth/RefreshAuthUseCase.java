package com.ggar.hibiki.core.orchestrator.auth;

import com.ggar.hibiki.core.identity.dto.RefreshAuthRequest;
import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.orchestrator.auth.mapper.RefreshRequestMapper;
import com.ggar.hibiki.core.orchestrator.auth.model.RefreshRequestDTO;
import com.ggar.hibiki.core.orchestrator.shared.BaseOrchestratorUseCase;
import com.ggar.hibiki.core.shared.auth.query.ValidateDeviceLoginQuery;
import com.ggar.hibiki.core.shared.mediator.Mediator;
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
        return mediator.send(refreshCommand).flatMap(authResponse -> {
            // 2: Validate the device
            ValidateDeviceLoginQuery deviceQuery = ValidateDeviceLoginQuery.builder()
                    .userId(authResponse.getUserId())
                    .deviceId(requestDto.getDeviceId())
                    .ip(ip)
                    .userAgent(userAgent)
                    .build();

            return mediator.send(deviceQuery).thenReturn(authResponse);
        });
    }
}
