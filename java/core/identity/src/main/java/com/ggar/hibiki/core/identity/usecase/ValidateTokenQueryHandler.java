package com.ggar.hibiki.core.identity.usecase;

import com.ggar.hibiki.core.identity.model.ValidateTokenQuery;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;

import java.util.Map;

public interface ValidateTokenQueryHandler extends QueryHandler<ValidateTokenQuery, Map<String, Object>> {
}
