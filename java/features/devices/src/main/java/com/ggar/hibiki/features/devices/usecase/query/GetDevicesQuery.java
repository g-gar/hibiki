package com.ggar.hibiki.features.devices.usecase.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.devices.domain.model.Device;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetDevicesQuery implements Query<List<Device>> {
    String userId;
}
