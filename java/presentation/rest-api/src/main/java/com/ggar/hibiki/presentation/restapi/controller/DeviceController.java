package com.ggar.hibiki.presentation.restapi.controller;

import com.ggar.hibiki.core.orchestrator.dto.DeviceDTO;
import com.ggar.hibiki.core.orchestrator.dto.RegisterDeviceRequestDTO;
import com.ggar.hibiki.core.orchestrator.dto.RevokeDeviceRequestDTO;
import com.ggar.hibiki.core.orchestrator.usecase.GetDevicesUseCase;
import com.ggar.hibiki.core.orchestrator.usecase.RegisterDeviceUseCase;
import com.ggar.hibiki.core.orchestrator.usecase.RevokeDeviceUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/devices")
@Tag(name = "Devices", description = "Endpoints for managing user devices")
@RequiredArgsConstructor
public class DeviceController {

    private final GetDevicesUseCase getDevicesUseCase;
    private final RegisterDeviceUseCase registerDeviceUseCase;
    private final RevokeDeviceUseCase revokeDeviceUseCase;

    @GetMapping
    @Operation(summary = "Get all devices for a user")
    public Mono<List<DeviceDTO>> getDevices(@RequestParam UUID userId) {
        // In a real scenario, userId would come from the JWT context
        return getDevicesUseCase.execute(userId);
    }

    @PostMapping
    @Operation(summary = "Register a new device")
    public Mono<DeviceDTO> registerDevice(@RequestBody RegisterDeviceRequestDTO request) {
        return registerDeviceUseCase.execute(request);
    }

    @DeleteMapping("/{deviceId}")
    @Operation(summary = "Revoke a device")
    public Mono<Void> revokeDevice(@PathVariable UUID deviceId, @RequestParam UUID userId) {
        // In a real scenario, userId would come from the JWT context
        return revokeDeviceUseCase.execute(RevokeDeviceRequestDTO.builder()
                .userId(userId)
                .deviceId(deviceId)
                .build());
    }
}
