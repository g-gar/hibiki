package com.ggar.hibiki.features.devices.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.devices.dto.RevokeDeviceCommand;

/**
 * Inbound service for handling device revocation commands.
 */
public interface RevokeDeviceCommandHandler extends CommandHandler<RevokeDeviceCommand, Void> {}
