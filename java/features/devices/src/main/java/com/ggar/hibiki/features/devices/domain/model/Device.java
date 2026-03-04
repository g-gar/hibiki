package com.ggar.hibiki.features.devices.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

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
