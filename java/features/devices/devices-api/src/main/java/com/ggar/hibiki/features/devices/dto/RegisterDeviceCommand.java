package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.DeviceType;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterDeviceCommand implements Command<Device> {
    UUID id;
    UUID userId;
    String name;
    DeviceType type;
    String userAgent;
    String ip;
}
