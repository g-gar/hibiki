package com.ggar.hibiki.features.devices.usecase.command;

import com.ggar.hibiki.core.shared.mediator.Command;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RevokeDeviceCommand implements Command<Void> {
    String deviceId;
    String userId;
}
