package com.ggar.hibiki.features.devices.service;

import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.devices.dto.ValidateDeviceLoginQuery;

/**
 * Interface for the handler responsible for validating device login attempts.
 */
public interface ValidateDeviceLoginQueryHandler extends QueryHandler<ValidateDeviceLoginQuery, Boolean> {}
