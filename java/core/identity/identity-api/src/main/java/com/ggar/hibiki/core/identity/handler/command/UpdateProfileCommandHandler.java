package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that updates user profiles.
 */
public interface UpdateProfileCommandHandler extends CommandHandler<UpdateProfileCommandHandler.Update, User> {

    /**
     * Data needed to update a profile.
     */
    record Update(String userId, String username, String email) implements Command<User> {}

    /**
     * Event emitted when a user's profile is updated.
     */
    record Updated(UUID userId, String username, String email) implements DomainEvent {}
}
