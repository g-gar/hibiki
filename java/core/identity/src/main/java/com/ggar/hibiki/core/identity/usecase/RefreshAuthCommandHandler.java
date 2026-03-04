package com.ggar.hibiki.core.identity.usecase;

import com.ggar.hibiki.core.identity.model.AuthResponse;
import com.ggar.hibiki.core.identity.model.RefreshAuthRequest;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;

public interface RefreshAuthCommandHandler extends CommandHandler<RefreshAuthRequest, AuthResponse> {
}
