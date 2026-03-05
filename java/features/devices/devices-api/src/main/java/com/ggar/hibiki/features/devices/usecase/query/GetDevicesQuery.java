package com.ggar.hibiki.features.devices.usecase.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.devices.domain.model.Device;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetDevicesQuery implements Query<List<Device>> {
    String userId;
}
