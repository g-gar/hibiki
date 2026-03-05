package com.ggar.hibiki.features.devices.domain.model;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Device {
    String id;
    String userId;
    String name;
    DeviceType type;
    DeviceStatus status;
    String userAgent;
    String lastIp;
    Instant createdAt;
    Instant lastSeenAt;
}
