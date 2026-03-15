package com.ggar.hibiki.core.identity.handler.query;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;

/**
 * Interface for the query handler that validates authentication tokens.
 */
public interface ValidateTokenQueryHandler extends QueryHandler<ValidateTokenQueryHandler.Validate, User> {

    /**
     * Data needed to validate a token.
     */
    record Validate(String token) implements Query<User> {}
}
