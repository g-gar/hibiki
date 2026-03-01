package com.ggar.hibiki.core.identity.usecase;

import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.identity.model.LoginRequest;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import reactor.core.publisher.Mono;

public interface LoginCommandHandler extends CommandHandler<LoginRequest, AuthResponse> {
}
