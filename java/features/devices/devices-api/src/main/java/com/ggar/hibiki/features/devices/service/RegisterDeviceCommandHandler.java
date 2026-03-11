package com.ggar.hibiki.features.devices.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.devices.dto.RegisterDeviceCommand;
import com.ggar.hibiki.features.devices.model.Device;

/**
 * Interface for the handler responsible for registering new devices.
 */
public interface RegisterDeviceCommandHandler extends CommandHandler<RegisterDeviceCommand, Device> {}
