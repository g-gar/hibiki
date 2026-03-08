package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.devices.model.Device;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetDevicesQuery implements Query<List<Device>> {
    UUID userId;
}
