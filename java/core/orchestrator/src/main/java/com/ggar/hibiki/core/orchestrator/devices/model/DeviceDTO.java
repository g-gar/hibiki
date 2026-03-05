package com.ggar.hibiki.core.orchestrator.devices.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    private String id;
    private String userId;
    private String deviceId;
    private String friendlyName;
    private String platform;
    private String appVersion;
    private Instant lastSeenAt;
    private Instant createdAt;
    private boolean isActive;
}
