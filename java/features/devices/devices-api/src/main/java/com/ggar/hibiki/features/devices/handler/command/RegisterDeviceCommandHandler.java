package com.ggar.hibiki.features.devices.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.devices.model.Device;
import com.ggar.hibiki.features.devices.model.DeviceType;
import java.util.UUID;

/**
 * Interface for the handler responsible for registering new devices.
 */
public interface RegisterDeviceCommandHandler extends CommandHandler<RegisterDeviceCommandHandler.Register, Device> {

    /**
     * Command to register a new device for a user.
     */
    record Register(UUID id, UUID userId, String name, DeviceType type, String userAgent, String ip)
            implements Command<Device> {}

    /**
     * Event published when a device is successfully registered.
     */
    record Registered(UUID deviceId, UUID userId, String name) implements DomainEvent {}
}
