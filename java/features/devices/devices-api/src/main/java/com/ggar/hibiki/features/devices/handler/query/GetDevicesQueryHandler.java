package com.ggar.hibiki.features.devices.handler.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.devices.model.Device;
import java.util.List;
import java.util.UUID;

/**
 * Interface for the handler responsible for retrieving user devices.
 */
public interface GetDevicesQueryHandler extends QueryHandler<GetDevicesQueryHandler.Get, List<Device>> {

    /**
     * Query to retrieve all devices associated with a user.
     */
    record Get(UUID userId) implements Query<List<Device>> {}
}
