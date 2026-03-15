package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that manages user login.
 */
public interface LoginCommandHandler extends CommandHandler<LoginCommandHandler.Login, User> {

    /**
     * Data needed to perform a login.
     */
    record Login(String username, String password, String deviceId, String ip, String userAgent)
            implements Command<User> {}

    /**
     * Event emitted when a user logs in successfully.
     */
    record LoggedIn(UUID userId, String deviceId) implements DomainEvent {}
}
