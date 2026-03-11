package com.ggar.hibiki.features.devices.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.devices.dto.RevokeDeviceCommand;

/**
 * Interface for the handler responsible for revoking device access.
 */
public interface RevokeDeviceCommandHandler extends CommandHandler<RevokeDeviceCommand, Void> {}
