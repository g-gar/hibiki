package com.ggar.hibiki.features.devices.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Device {
    UUID id;
    UUID userId;
    String name;
    DeviceType type;
    DeviceStatus status;
    String userAgent;
    String lastIp;
    Instant createdAt;
    Instant lastSeenAt;
}
