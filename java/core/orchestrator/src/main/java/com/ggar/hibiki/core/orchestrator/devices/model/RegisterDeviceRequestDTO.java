package com.ggar.hibiki.core.orchestrator.devices.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDeviceRequestDTO {
    private String userId;
    private String deviceId;
    private String friendlyName;
    private String platform;
    private String appVersion;
}
