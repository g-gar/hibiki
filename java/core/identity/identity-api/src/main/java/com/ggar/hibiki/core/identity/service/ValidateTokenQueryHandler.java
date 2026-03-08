package com.ggar.hibiki.core.identity.service;

import com.ggar.hibiki.core.identity.dto.ValidateTokenQuery;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import java.util.Map;

public interface ValidateTokenQueryHandler extends QueryHandler<ValidateTokenQuery, Map<String, Object>> {}
