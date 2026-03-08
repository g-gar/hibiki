package com.ggar.hibiki.core.identity.service;

import com.ggar.hibiki.core.identity.dto.LoginRequest;
import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;

public interface LoginCommandHandler extends CommandHandler<LoginRequest, AuthResponse> {}
