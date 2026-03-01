package com.ggar.hibiki.core.identity.usecase;

import com.ggar.hibiki.core.identity.model.TwoFactorRequest;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import reactor.core.publisher.Mono;

public interface Manage2FACommandHandler extends CommandHandler<TwoFactorRequest, Void> {
}
