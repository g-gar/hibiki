package com.ggar.hibiki.core.orchestrator.devices;

import com.ggar.hibiki.core.orchestrator.devices.mapper.DeviceOrchestratorMapper;
import com.ggar.hibiki.core.orchestrator.devices.model.DeviceDTO;
import com.ggar.hibiki.core.orchestrator.devices.model.RegisterDeviceRequestDTO;
import com.ggar.hibiki.core.orchestrator.shared.BaseOrchestratorUseCase;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.devices.domain.model.DeviceType;
import com.ggar.hibiki.features.devices.usecase.command.RegisterDeviceCommand;
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

        return mediator.send(RegisterDeviceCommand.builder()
                .userId(request.getUserId())
                .id(request.getDeviceId())
                .name(request.getFriendlyName())
                .type(type)
                .build())
                .map(mapper::toDto);
    }
}
