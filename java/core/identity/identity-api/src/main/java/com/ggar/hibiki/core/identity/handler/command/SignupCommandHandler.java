package com.ggar.hibiki.core.identity.handler.command;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that manages user signups.
 */
public interface SignupCommandHandler extends CommandHandler<SignupCommandHandler.Signup, User> {

    /**
     * Data needed to sign up a new user.
     */
    record Signup(String username, String email, String password) implements Command<User> {}

    /**
     * Event published when a user successfully signs up.
     */
    record SignedUp(UUID userId, String username, String email) implements DomainEvent {}
}
