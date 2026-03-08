package com.ggar.hibiki.features.devices.service;

import com.ggar.hibiki.core.shared.auth.query.ValidateDeviceLoginQuery;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;

/**
 * Inbound service for handling device login validation queries.
 */
public interface ValidateDeviceLoginQueryHandler extends QueryHandler<ValidateDeviceLoginQuery, Boolean> {}
