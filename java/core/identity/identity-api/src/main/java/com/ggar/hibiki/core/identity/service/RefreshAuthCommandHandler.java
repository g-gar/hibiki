package com.ggar.hibiki.core.identity.service;

import com.ggar.hibiki.core.identity.dto.AuthResponse;
import com.ggar.hibiki.core.identity.dto.RefreshAuthRequest;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;

public interface RefreshAuthCommandHandler extends CommandHandler<RefreshAuthRequest, AuthResponse> {}
