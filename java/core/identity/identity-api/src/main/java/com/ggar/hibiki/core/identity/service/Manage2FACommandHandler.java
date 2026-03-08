package com.ggar.hibiki.core.identity.service;

import com.ggar.hibiki.core.identity.dto.TwoFactorRequest;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;

public interface Manage2FACommandHandler extends CommandHandler<TwoFactorRequest, Void> {}
