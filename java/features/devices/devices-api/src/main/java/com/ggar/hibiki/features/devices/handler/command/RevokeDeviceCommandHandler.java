package com.ggar.hibiki.features.devices.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the handler responsible for revoking device access.
 */
public interface RevokeDeviceCommandHandler extends CommandHandler<RevokeDeviceCommandHandler.Revoke, Void> {

    /**
     * Command to revoke access for a specific device.
     */
    record Revoke(UUID userId, UUID deviceId) implements Command<Void> {}

    /**
     * Event published when a device is successfully revoked.
     */
    record Revoked(UUID deviceId, UUID userId) implements DomainEvent {}
}
