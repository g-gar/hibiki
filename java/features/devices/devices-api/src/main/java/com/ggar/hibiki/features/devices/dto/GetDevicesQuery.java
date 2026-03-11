package com.ggar.hibiki.features.devices.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.devices.model.Device;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Query to retrieve all devices associated with a user.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetDevicesQuery implements Query<List<Device>> {
    /**
     * User identity identifier.
     */
    UUID userId;
}
