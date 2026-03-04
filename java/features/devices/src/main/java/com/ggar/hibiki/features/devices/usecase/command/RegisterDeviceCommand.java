package com.ggar.hibiki.features.devices.usecase.command;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.devices.domain.model.Device;
import com.ggar.hibiki.features.devices.domain.model.DeviceType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterDeviceCommand implements Command<Device> {
    String id;
    String userId;
    String name;
    DeviceType type;
    String userAgent;
    String ip;
}
