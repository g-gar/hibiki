package com.ggar.hibiki.core.orchestrator.devices.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    private UUID id;
    private UUID userId;
    private UUID deviceId;
    private String friendlyName;
    private String platform;
    private String appVersion;
    private Instant lastSeenAt;
    private Instant createdAt;
    private boolean isActive;
}
