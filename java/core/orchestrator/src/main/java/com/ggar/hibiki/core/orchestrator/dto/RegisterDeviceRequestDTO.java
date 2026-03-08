package com.ggar.hibiki.core.orchestrator.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDeviceRequestDTO {
    private UUID userId;
    private UUID deviceId;
    private String friendlyName;
    private String platform;
    private String appVersion;
}
