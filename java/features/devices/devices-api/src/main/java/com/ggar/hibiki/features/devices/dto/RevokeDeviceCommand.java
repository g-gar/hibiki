package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RevokeDeviceCommand implements Command<Void> {
    UUID deviceId;
    UUID userId;
}
