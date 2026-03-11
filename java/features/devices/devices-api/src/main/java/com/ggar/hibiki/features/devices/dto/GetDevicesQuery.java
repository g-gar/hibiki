package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.IdentityContext;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Query to retrieve all devices associated with a user.
 */
@Value
@Builder(toBuilder = true)
@With
public class GetDevicesQuery implements Query<List<Device>> {
    /**
     * User identity context.
     */
    IdentityContext identityContext;
}
