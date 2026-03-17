package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that manages auth token refreshes.
 */
public interface RefreshAuthCommandHandler extends CommandHandler<RefreshAuthCommandHandler.Refresh, User> {

    /**
     * Data needed to refresh auth tokens.
     */
    record Refresh(String refreshToken, String deviceId) implements Command<User> {}

    /**
     * Event emitted when authentication tokens are refreshed.
     */
    record Refreshed(UUID userId, String deviceId) implements DomainEvent {}
}
