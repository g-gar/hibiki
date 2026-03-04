package com.ggar.hibiki.core.orchestrator.devices.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevokeDeviceRequestDTO {
    private String userId;
    private String deviceId;
}
