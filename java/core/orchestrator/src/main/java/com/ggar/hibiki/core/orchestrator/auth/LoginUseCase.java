package com.ggar.hibiki.core.orchestrator.auth;

import com.ggar.hibiki.core.identity.dto.LoginRequest;
import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.orchestrator.auth.mapper.LoginRequestMapper;
import com.ggar.hibiki.core.orchestrator.auth.model.LoginRequestDTO;
import com.ggar.hibiki.core.orchestrator.shared.BaseOrchestratorUseCase;
import com.ggar.hibiki.core.shared.auth.query.ValidateDeviceLoginQuery;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * High-level Use Case orchestrating the Login flow.
 * Combines credentials validation (core:identity) with device validation
 * (features:devices).
 */
@Service
public class LoginUseCase extends BaseOrchestratorUseCase {

    private final LoginRequestMapper mapper;

    public LoginUseCase(Mediator mediator, LoginRequestMapper mapper) {
        super(mediator);
        this.mapper = mapper;
    }

    public Mono<AuthResponse> execute(LoginRequestDTO requestDto, String ip, String userAgent) {
        // Map DTO to Domain Command
        LoginRequest loginCommand = mapper.toCommand(requestDto, ip, userAgent);

        // Step 1: Execute Login Command (Validates credentials and generates tokens)
        return mediator.send(loginCommand).flatMap(authResponse -> {
            // Step 2: Validate Device (Requires user ID from the response)
            ValidateDeviceLoginQuery deviceQuery = ValidateDeviceLoginQuery.builder()
                    .userId(authResponse.getUserId())
                    .deviceId(requestDto.getDeviceId())
                    .ip(ip)
                    .userAgent(userAgent)
                    .build();

            // Chain the checks: if device validation passes, return the original
            // authResponse
            return mediator.send(deviceQuery).thenReturn(authResponse);
        });
    }
}
