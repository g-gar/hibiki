package com.ggar.hibiki.core.identity.service;

import com.ggar.hibiki.core.identity.dto.SignupRequest;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;

public interface SignupCommandHandler extends CommandHandler<SignupRequest, Void> {}
