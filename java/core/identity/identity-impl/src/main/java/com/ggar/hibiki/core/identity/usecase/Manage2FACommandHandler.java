package com.ggar.hibiki.core.identity.usecase;

import com.ggar.hibiki.core.identity.model.TwoFactorRequest;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;

public interface Manage2FACommandHandler extends CommandHandler<TwoFactorRequest, Void> {}
