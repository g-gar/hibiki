package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.orchestrator.dto.DeviceDTO;
import com.ggar.hibiki.core.orchestrator.dto.RegisterDeviceRequestDTO;
import com.ggar.hibiki.core.orchestrator.mapper.DeviceOrchestratorMapper;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.devices.handler.command.RegisterDeviceCommandHandler;
import com.ggar.hibiki.features.devices.model.DeviceType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RegisterDeviceUseCase extends BaseOrchestratorUseCase {

    private final DeviceOrchestratorMapper mapper;

    public RegisterDeviceUseCase(Mediator mediator, DeviceOrchestratorMapper mapper) {
        super(mediator);
        this.mapper = mapper;
    }

    public Mono<DeviceDTO> execute(RegisterDeviceRequestDTO request) {
        DeviceType type = DeviceType.UNKNOWN;
        if (request.getPlatform() != null) {
            try {
                type = DeviceType.valueOf(request.getPlatform().toUpperCase());
            } catch (IllegalArgumentException e) {
                // TODO: Ignore and use UNKNOWN
            }
        }

        RegisterDeviceCommandHandler.Register command = new RegisterDeviceCommandHandler.Register(
                request.getDeviceId(),
                request.getUserId(),
                request.getFriendlyName(),
                type,
                null, // userAgent (if available in request)
                null); // ip (if available in request)

        return Mono.from(mediator.send(command)).map(mapper::toDto);
    }
}
