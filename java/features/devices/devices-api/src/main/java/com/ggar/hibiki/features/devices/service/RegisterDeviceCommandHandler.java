package com.ggar.hibiki.features.devices.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.devices.dto.RegisterDeviceCommand;
import com.ggar.hibiki.features.devices.model.Device;

/**
 * Inbound service for handling device registration commands.
 */
public interface RegisterDeviceCommandHandler extends CommandHandler<RegisterDeviceCommand, Device> {}
