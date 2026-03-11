package com.ggar.hibiki.features.devices.service;

import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.devices.dto.GetDevicesQuery;
import com.ggar.hibiki.features.devices.model.Device;
import java.util.List;

/**
 * Interface for the handler responsible for retrieving user devices.
 */
public interface GetDevicesQueryHandler extends QueryHandler<GetDevicesQuery, List<Device>> {}
