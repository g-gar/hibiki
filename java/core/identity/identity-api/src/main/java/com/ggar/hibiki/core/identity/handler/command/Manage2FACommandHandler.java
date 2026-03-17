package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that manages 2FA settings.
 */
public interface Manage2FACommandHandler extends CommandHandler<Manage2FACommandHandler.Manage, Void> {

    /**
     * Data needed to manage 2FA for a user.
     */
    record Manage(String userId, boolean enabled) implements Command<Void> {}

    /**
     * Event emitted when 2FA settings are changed.
     */
    record Managed(UUID userId, boolean enabled) implements DomainEvent {}
}
