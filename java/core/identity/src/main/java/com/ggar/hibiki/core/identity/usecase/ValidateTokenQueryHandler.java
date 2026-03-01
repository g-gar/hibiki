package com.ggar.hibiki.core.identity.usecase;

import com.ggar.hibiki.core.identity.model.ValidateTokenQuery;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import io.jsonwebtoken.Claims;

public interface ValidateTokenQueryHandler extends QueryHandler<ValidateTokenQuery, Claims> {
}
