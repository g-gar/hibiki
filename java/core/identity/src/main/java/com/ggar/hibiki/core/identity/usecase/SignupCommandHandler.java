package com.ggar.hibiki.core.identity.usecase;

import com.ggar.hibiki.core.identity.model.SignupRequest;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;

public interface SignupCommandHandler extends CommandHandler<SignupRequest, Void> {
}
